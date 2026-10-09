"""Compile both roles and compare real file outputs with independent models."""
import argparse
import hashlib
import json
import os
from pathlib import Path
import random
import subprocess
import tempfile

ROOT = Path(__file__).resolve().parents[1]
PACKS = {
    'US18-Counting-Haybales': 'haybales',
    'US21-Priority-Queues': 'priority',
    'US22-Prefix-Sums': 'prefix',
}


def hay_input(positions, intervals):
    return f'{len(positions)} {len(intervals)}\n' + ' '.join(map(str, positions)) + '\n' + ''.join(f'{a} {b}\n' for a, b in intervals)


def prefix_input(values, intervals):
    return f'{len(values)} {len(intervals)}\n' + ' '.join(map(str, values)) + '\n' + ''.join(f'{a} {b}\n' for a, b in intervals)


def priority_input(tasks):
    return str(len(tasks)) + '\n' + ''.join(f'{priority} {label}\n' for priority, label in tasks)


def fixtures(folder):
    rng = random.Random(666)
    if folder == 'US18-Counting-Haybales':
        cases = [([3, 2, 7, 5], [(2, 3), (2, 4), (2, 5), (2, 7), (4, 6), (8, 10)]),
                 ([0], [(0, 0), (1, 1), (0, 1000000000)]),
                 ([1000000000], [(0, 0), (999999999, 999999999), (1000000000, 1000000000)]),
                 ([1, 5], [(0, 0), (0, 1), (1, 1), (1, 5), (2, 4), (5, 5), (6, 9)])]
        for _ in range(100):
            positions = rng.sample(range(51), rng.randint(1, 35))
            intervals = [(a, b) for a in range(0, 56, 5) for b in range(a, 56, 5)]
            cases.append((positions, intervals))
        for positions, intervals in cases:
            yield hay_input(positions, intervals), [sum(a <= value <= b for value in positions) for a, b in intervals]
        # Full advertised scale; uniform facts give an independent closed-form oracle.
        n = 100000
        intervals = [(0, 1000000000) if i % 3 == 0 else (2*i, 2*i) if i % 3 == 1 else (999999999, 1000000000) for i in range(n)]
        yield hay_input(list(reversed(range(0, 2*n, 2))), intervals), [n if i % 3 == 0 else 1 if i % 3 == 1 else 0 for i in range(n)]
    elif folder == 'US22-Prefix-Sums':
        cases = [([], [(0, 0)]), ([3, -2, 7, 0, 4], [(0, 0), (1, 2), (0, 5), (2, 4), (5, 5)]),
                 ([1000000000]*4, [(0, 4), (1, 3)]), ([-1000000000]*4, [(0, 4), (2, 2)]), ([], [])]
        for _ in range(100):
            values = [rng.randint(-20, 20) for _ in range(rng.randint(0, 25))]
            intervals = [(a, b) for a in range(len(values)+1) for b in range(a, len(values)+1)]
            cases.append((values, intervals))
        for values, intervals in cases:
            yield prefix_input(values, intervals), [sum(values[a:b]) for a, b in intervals]
        n = 100000
        intervals = [(0, n) if i % 3 == 0 else (i, i+1) if i % 3 == 1 else (i, i) for i in range(n)]
        yield prefix_input([1000000000]*n, intervals), [100000000000000 if i % 3 == 0 else 1000000000 if i % 3 == 1 else 0 for i in range(n)]
    else:
        cases = [[], [(5, 'one')], [(2, 'beta'), (1, 'alpha'), (1, 'gamma'), (-2, 'urgent'), (2, 'beta')],
                 [(1, 'z'), (1, 'a'), (1, 'z')], [(-9223372036854775808, 'min'), (9223372036854775807, 'max'), (0, 'middle')]]
        for _ in range(100):
            cases.append([(rng.randint(-4, 4), rng.choice(['z', 'a', 'repeat'])) for _ in range(rng.randint(0, 50))])
        cases.append([((i*7919) % 101 - 50, 'task'+str(i)) for i in range(100000)])
        for tasks in cases:
            # Python's documented stable sort retains original record order on ties.
            yield priority_input(tasks), [label for _, label in sorted(tasks, key=lambda row: row[0])]


def verify(compiler, mode):
    flags = ['-std=c++20', '-Wall', '-Wextra', '-Wpedantic', '-Werror', '-O1']
    if mode == 'sanitized':
        flags += ['-g', '-fsanitize=address,undefined', '-fno-omit-frame-pointer']
        if os.uname().sysname == 'Linux':
            flags += ['-fno-pie', '-no-pie']
    leaks = '1' if os.uname().sysname == 'Linux' else '0'
    environment = {**os.environ, 'ASAN_OPTIONS': 'detect_leaks='+leaks+':halt_on_error=1', 'UBSAN_OPTIONS': 'halt_on_error=1:print_stacktrace=1'}
    results = []
    with tempfile.TemporaryDirectory(prefix='silver-native-') as directory:
        working = Path(directory)
        for folder, basename in PACKS.items():
            binary = working / (basename+'-reference')
            learner = working / (basename+'-learner')
            reference_source = ROOT / folder / 'solution/main.cpp'
            starter_source = ROOT / folder / 'starter/main.cpp'
            subprocess.run([compiler, *flags, str(reference_source), '-o', str(binary)], check=True, timeout=90)
            subprocess.run([compiler, *flags, str(starter_source), '-o', str(learner)], check=True, timeout=90)
            input_file, output_file = working/(basename+'.in'), working/(basename+'.out')
            count = queries = 0
            for text, expected in fixtures(folder):
                input_file.write_text(text)
                output_file.unlink(missing_ok=True)
                process = subprocess.run([str(binary)], cwd=working, env=environment, capture_output=True, text=True, timeout=15)
                assert process.returncode == 0, (folder, count, process.stderr)
                actual = output_file.read_text().split()
                assert actual == list(map(str, expected)), (folder, count, actual[:15], expected[:15])
                count += 1
                queries += len(expected)
            input_file.write_bytes((ROOT/folder/'starter'/(basename+'.in')).read_bytes())
            output_file.unlink(missing_ok=True)
            process = subprocess.run([str(learner)], cwd=working, env=environment, capture_output=True, text=True, timeout=15)
            assert process.returncode == 2 and 'Complete ' in process.stderr, (folder, process.returncode, process.stderr)
            assert not output_file.exists(), 'Untouched starter must not create a completed answer'
            results.append({'folder': folder, 'fileIOCases': count, 'outputRecords': queries,
                            'independentOracle': True, 'maximumSizeCase': True, 'starterUnfinished': True,
                            'referenceSha256': hashlib.sha256(reference_source.read_bytes().replace(b'\r\n', b'\n')).hexdigest(),
                            'starterSha256': hashlib.sha256(starter_source.read_bytes().replace(b'\r\n', b'\n')).hexdigest()})
    print(json.dumps({'compiler': compiler, 'mode': mode, 'packs': results}, sort_keys=True))


if __name__ == '__main__':
    parser = argparse.ArgumentParser()
    parser.add_argument('--compiler', default='c++')
    parser.add_argument('--mode', choices=['ordinary', 'sanitized'], default='ordinary')
    args = parser.parse_args()
    verify(args.compiler, args.mode)
