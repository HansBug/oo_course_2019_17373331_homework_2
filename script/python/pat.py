from sympy import diff, expand
from sympy.abc import x
import os
import subprocess
import random
import re

os.chdir('out')


def get_java_output(input: str) -> str:
    popen = subprocess.Popen(['java', 'homework.Main'],
                             stdin=subprocess.PIPE, stdout=subprocess.PIPE)
    out, err = popen.communicate(input=bytes(input.encode('utf-8')))
    assert err is None
    return str(out.decode()).strip()


def assert_equals(input: str):
    print('in:', input)
    input_styled = input
    input_styled = input_styled.replace("^", "**")
    try:
        expected = diff(eval(input_styled))
    except Exception as e:
        expected = "WRONG FORMAT!"
    actual = get_java_output(input=input).replace("^", "**")

    print('exp:', expected)
    print('act:', actual)
    if expected == "WRONG FORMAT!":
        assert actual == expected
        return

    if actual == "WRONG FORMAT!":
        assert re.search(r'[-+][\ \t]*[-+][\ \t]+[0-9]', input) is not None or '\f' in input or '\\' in input
        # print("input:", input)
        # print('actual is WF, but py expected', expected)
        return

    actual = eval(actual)

    for i in range(20):
        if not isinstance(expected, int):
            expected = expected.subs(x, i)
        if not isinstance(actual, int):
            actual = actual.subs(x, i)
        assert expected == actual


def sample_generator(num_sample=500):
    blank_pool = (['\t', ' ', '    ', '\t ',] + [''] * 15) * 2 + ['abc', '\f', '你爸爸', '\\']
    for _ in range(num_sample):
        sample = 1
        for _ in range(10):
            sample += random.choice([21283, -1439, 0])
            sample *= random.choice([47443, -23232, 1, x])
        sample = str(sample.expand() if not isinstance(sample, int) else sample).replace(' ', '').replace('**', '^')

        i = 0
        while i < len(sample):
            if sample[i] in ['+', '-']:
                chosen = random.choice(['+', '-', ''])
                sample = sample[:i] + chosen + sample[i:]
                if not chosen == '':
                    i+=1
            i += 1

        signs = ['+', '-', '*', '^']
        l = len(sample)
        for i in range(l):
            if i+1 < l and (sample[i+1] in signs) or (sample[i] in signs):
                sample = sample[:i+1] + \
                    random.choice(blank_pool) + sample[i+1:]
        yield sample


def main():
    for i, sample in enumerate(sample_generator()):
        print('sample', i)
        assert_equals(sample)


if __name__ == '__main__':
    main()
