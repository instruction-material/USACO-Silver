"""Verify standalone search learners against independent models and actual guides."""
import argparse
import datetime
import hashlib
import json
import math
import os
import re
import shutil
import signal
import subprocess
import tempfile
from collections import deque
from pathlib import Path

ROOT = Path(__file__).resolve().parents[1]
TASK = os.environ.get('CODEX_PARENT_TASK_ID', 'required-search-learners')


def event(name, **fields):
    print(json.dumps(dict(event=name, parentTaskId=TASK, time=datetime.datetime.now(datetime.timezone.utc).isoformat(), **fields)), flush=True)


def run(args, cwd, expected=0):
    child = subprocess.Popen(args, cwd=cwd, stdout=subprocess.PIPE, stderr=subprocess.PIPE, start_new_session=True)
    event('start', command=args, cwd=str(cwd), pid=child.pid, timeoutSeconds=30)
    try:
        out, err = child.communicate(timeout=30)
    except BaseException:
        os.killpg(child.pid, signal.SIGTERM)
        try:
            child.communicate(timeout=5)
        except subprocess.TimeoutExpired:
            os.killpg(child.pid, signal.SIGKILL)
            child.communicate()
        event('child-process-group-cleanup', pid=child.pid)
        raise
    finally:
        event('end', pid=child.pid, exitCode=child.returncode)
    if expected is not None:
        assert child.returncode == expected, (args, child.returncode, out.decode(errors='replace'), err.decode(errors='replace'))
    return child.returncode, out, err


def bucket_answer(capacity):
    first = (0, 0, capacity[2])
    seen, pending = {first}, deque([first])
    answers = set()
    while pending:
        state = pending.popleft()
        if state[0] == 0:
            answers.add(state[2])
        for source in range(3):
            for destination in range(3):
                if source == destination:
                    continue
                move = min(state[source], capacity[destination] - state[destination])
                next_state = list(state)
                next_state[source] -= move
                next_state[destination] += move
                next_state = tuple(next_state)
                if next_state not in seen:
                    seen.add(next_state)
                    pending.append(next_state)
    return (' '.join(map(str, sorted(answers))) + '\n').encode()


def prime(number):
    return number >= 2 and all(number % divisor for divisor in range(2, math.isqrt(number) + 1))


def brute_prime_answer(lower, upper):
    return ''.join(f'{number}\n' for number in range(lower, upper + 1) if str(number) == str(number)[::-1] and prime(number)).encode()


def candidate_model(digits):
    # Independent half-string enumeration, including even seeds before filtering.
    half = (digits + 1) // 2
    values = []
    for seed in range(10 ** (half - 1), 10 ** half):
        left = str(seed)
        number = int(left + (left[:-1] if digits % 2 else left)[::-1])
        if number % 2:
            values.append(number)
    return values


def full_prime_answer():
    # Sieve prime divisors separately from the reference's per-number divisor loop.
    sieve = [True] * 10001
    sieve[0] = sieve[1] = False
    for value in range(2, 101):
        if sieve[value]:
            for multiple in range(value * value, len(sieve), value):
                sieve[multiple] = False
    divisors = [value for value, is_prime in enumerate(sieve) if is_prime]
    answer = []
    for digits in range(1, 9):
        for number in candidate_model(digits):
            if number < 5:
                continue
            valid = True
            for divisor in divisors:
                if divisor * divisor > number:
                    break
                if number % divisor == 0:
                    valid = False
                    break
            if valid:
                answer.append(number)
    return ''.join(f'{number}\n' for number in answer).encode()


MILK_PROBE = '''
class Probe {
    public static void main(String[] args) {
        int count = 0;
        for (int[] capacity : new int[][] {{8,9,10},{1,1,1},{20,20,20},{4,6,7}}) {
            for (int a=0; a<=capacity[0]; a++) for (int b=0; b<=capacity[1]; b++) {
                int c=capacity[2]-a-b;
                if (c<0 || c>capacity[2]) continue;
                for (int from=0; from<3; from++) for (int to=0; to<3; to++) {
                    if (from==to) continue;
                    Node original = new Node(a,b,c);
                    int[] before = original.milk.clone();
                    Node next = original.pour(original,from,to,capacity[to]);
                    int amount = Math.min(before[from],capacity[to]-before[to]);
                    if (!java.util.Arrays.equals(before,original.milk)) throw new AssertionError("input mutation");
                    if (next==original || next.milk==original.milk) throw new AssertionError("state alias");
                    for (int i=0;i<3;i++) {
                        int expected = before[i] + (i==to?amount:0) - (i==from?amount:0);
                        if (next.milk[i]!=expected || next.milk[i]<0 || next.milk[i]>capacity[i]) throw new AssertionError("pour invariant");
                    }
                    if (next.milk[0]+next.milk[1]+next.milk[2]!=capacity[2]) throw new AssertionError("milk loss");
                    count++;
                }
            }
        }
        TASK_PROBE
        System.out.println(count);
    }
}
'''
PRIME_PROBE = '''
class Probe {
    public static void main(String[] args) {
        for (int value : new int[] {-3,0,1,2,3,4,5,7,11,121,131,9973,100000000,Integer.MAX_VALUE}) {
            boolean expected = value>=2;
            for (int divisor=2; expected && (long)divisor*divisor<=value; divisor++) {
                if (value%divisor==0) expected=false;
            }
            if (pprime.isPrime(value)!=expected) throw new AssertionError("primality " + value);
        }
        TASK_PROBE
    }
}
'''


def main():
    parser = argparse.ArgumentParser()
    parser.add_argument('--java', required=True)
    parser.add_argument('--javac', required=True)
    args = parser.parse_args()
    fixture = json.loads((ROOT / 'tests/fixtures/required-search-learner-originals.json').read_text())
    for entry in fixture['files']:
        target = entry.get('archive', entry.get('unchanged'))
        data = run(['git', 'show', 'HEAD:' + target], ROOT)[1]
        assert hashlib.sha256(data).hexdigest() == entry['sha256'], target
        assert (ROOT / target).read_bytes().replace(b'\r\n', b'\n') == data.replace(b'\r\n', b'\n')
    tracked = run(['git', 'ls-files', '-z', '--', 'US6-Mothers-Milk', 'US10-Prime-Palindromes'], ROOT)[1]
    protected = {ROOT / name: hashlib.sha256((ROOT / name).read_bytes()).hexdigest() for name in tracked.decode().split('\0') if name}
    cases = {
        'milk3': [((8,9,10), bucket_answer((8,9,10))), *((capacity,bucket_answer(capacity)) for capacity in [(1,1,1),(20,20,20),(4,6,7),(20,1,20),(1,20,20),(2,3,4),(7,13,20)])],
        'pprime': [*((bounds,brute_prime_answer(*bounds)) for bounds in [(5,5),(11,11),(12,100),(5,500),(99,111),(100,2000),(9000,11000)]), ((5,100000000),full_prime_answer())]
    }
    malformed = {
        'milk3': ['', '8 9', 'eight 9 10', '8.5 9 10', '0 9 10', '8 21 10', '8 9 10 extra', '2147483648 9 10'],
        'pprime': ['', '5', 'five 500', '5.5 500', '4 500', '500 5', '5 100000001', '5 500 extra']
    }
    summary = dict(referenceCases=0, learnerRefusals=0, malformedLearnerRefusals=0, directTaskRefusals=0, documentedNativeGuards=0, failedCompilationGuards=0, missingEntryClassGuards=0, wrongSampleOracleReferenceGuards=0, helperRoles=0, maximumInterval=False)
    with tempfile.TemporaryDirectory(prefix='silver-search-learners-') as temporary:
        temporary = Path(temporary)
        for folder, entry in [('US6-Mothers-Milk','milk3'),('US10-Prime-Palindromes','pprime')]:
            input_name, output_name = entry+'.in', entry+'.out'
            for role in ['starter','solution']:
                work = temporary / (entry+'-'+role)
                shutil.copytree(ROOT / folder / role, work)
                source = (work/'Main.java').read_text()
                classes = work/'classes'
                classes.mkdir()
                run([args.javac,'--release','17','-encoding','UTF-8','-Xlint:all','-Werror','-d',str(classes),'Main.java'],work)
                assert not (work/output_name).exists()
                for values, expected in cases[entry]:
                    (work/input_name).write_text(' '.join(map(str,values))+'\n')
                    (work/output_name).write_bytes(b'Earlier saved answer\n')
                    result = run([args.java,'-cp',str(classes),entry],work,0 if role=='solution' else 2)
                    assert result[1] == b''
                    if role=='solution':
                        assert (work/output_name).read_bytes()==expected,(entry,values)
                        summary['referenceCases']+=1
                    else:
                        assert b'Unfinished learner task: TASK:' in result[2]
                        assert (work/output_name).read_bytes()==b'Earlier saved answer\n'
                        summary['learnerRefusals']+=1
                if entry == 'pprime' and role == 'solution':
                    summary['maximumInterval'] = True
                if role == 'starter':
                    for text in malformed[entry]:
                        (work/input_name).write_text(text + '\n')
                        (work/output_name).write_bytes(b'Earlier saved answer\n')
                        result = run([args.java,'-cp',str(classes),entry],work,2)
                        assert result[1] == b'' and b'Invalid input:' in result[2]
                        assert (work/output_name).read_bytes() == b'Earlier saved answer\n'
                        summary['malformedLearnerRefusals'] += 1
                if entry=='milk3':
                    task = 'try { milk3.reachableVolumes(new int[] {8,9,10}); throw new AssertionError("missing refusal"); } catch (UnsupportedOperationException expected) { if (!expected.getMessage().contains("TASK:")) throw expected; }' if role=='starter' else ''
                    probe = MILK_PROBE.replace('TASK_PROBE',task)
                else:
                    task = 'try { pprime.generatePalindromes(1); throw new AssertionError("missing refusal"); } catch (UnsupportedOperationException expected) { if (!expected.getMessage().contains("TASK:")) throw expected; }' if role=='starter' else 'for (int digits=1;digits<=8;digits++) { for (int value:pprime.generatePalindromes(digits)) System.out.print(value+" "); System.out.println(); }'
                    probe = PRIME_PROBE.replace('TASK_PROBE',task)
                (work/'Main.java').write_text(source+probe)
                run([args.javac,'--release','17','-encoding','UTF-8','-Xlint:all','-Werror','-d',str(classes),'Main.java'],work)
                output = run([args.java,'-cp',str(classes),'Probe'],work)[1]
                if entry=='milk3':
                    expected_count = sum(6 for capacity in [(8,9,10),(1,1,1),(20,20,20),(4,6,7)] for a in range(capacity[0]+1) for b in range(capacity[1]+1) if 0<=capacity[2]-a-b<=capacity[2])
                    assert int(output)==expected_count
                elif role=='solution':
                    lists=[list(map(int,line.split())) for line in output.decode().splitlines()]
                    assert len(lists)==8 and sum(map(len,lists))==11110
                    for digits,values in enumerate(lists,1):
                        assert values==candidate_model(digits)
                        if digits<=4:
                            assert values==[number for number in range(10**(digits-1),10**digits) if number%2 and str(number)==str(number)[::-1]]
                summary['helperRoles']+=1
                if role=='starter':summary['directTaskRefusals']+=1
                guide=(ROOT/folder/role/'README.md').read_text()
                commands=re.findall(r'```sh\n(.*?)\n```',guide,re.S)
                assert len(commands)==1
                command=commands[0]
                variants = ['normal','broken-compile','missing-entry']
                if role == 'solution':
                    variants.append('wrong-oracle')
                for variant in variants:
                    guarded=temporary/(entry+'-'+role+'-'+variant)
                    shutil.copytree(ROOT/folder/role,guarded)
                    # Prove stale classes cannot survive the printed guard.
                    (guarded/'target/classes').mkdir(parents=True)
                    run([args.javac,'--release','17','-d','target/classes','Main.java'],guarded)
                    if variant=='broken-compile':(guarded/'Main.java').write_text(source+'\nThis is not Java\n')
                    elif variant=='missing-entry':(guarded/'Main.java').write_text(source.replace('class '+entry+' {','class MissingEntry {',1))
                    elif variant=='wrong-oracle':(guarded/'sample.expected').write_bytes(b'Intentionally wrong expected answer\n')
                    (guarded/output_name).write_bytes(b'Earlier saved answer\n')
                    script=command.replace('javac --release',json.dumps(args.javac)+' --release').replace('java -cp',json.dumps(args.java)+' -cp')
                    result=run(['bash','-c',script],guarded,expected=None)
                    if variant=='normal' and role=='solution':
                        assert result[0]==0 and (guarded/output_name).read_bytes()==(guarded/'sample.expected').read_bytes()
                    else:
                        assert result[0]!=0
                        assert (guarded/output_name).read_bytes()==b'Earlier saved answer\n'
                    assert not (guarded/(output_name+'.new')).exists()
                    if variant=='normal':summary['documentedNativeGuards']+=1
                    elif variant=='broken-compile':summary['failedCompilationGuards']+=1
                    elif variant=='missing-entry':summary['missingEntryClassGuards']+=1
                    else:summary['wrongSampleOracleReferenceGuards']+=1
    assert all(hashlib.sha256(path.read_bytes()).hexdigest()==digest for path,digest in protected.items())
    assert summary['referenceCases']==summary['learnerRefusals']==16
    assert summary['malformedLearnerRefusals']==16
    assert summary['directTaskRefusals']==2
    assert summary['wrongSampleOracleReferenceGuards']==2
    assert summary['maximumInterval']
    assert summary['documentedNativeGuards']==summary['failedCompilationGuards']==summary['missingEntryClassGuards']==summary['helperRoles']==4
    event('verified-required-search-learners', **summary, originalGitBlobsPreserved=len(fixture['files']), sourceFilesUnchanged=True, runtime=os.environ.get('TASK_JDK','local'))


if __name__=='__main__':
    main()
