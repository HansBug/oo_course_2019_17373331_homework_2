from sympy import *
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
    input_styled = input_styled.replace("^", " ** ")
    try:
        expected = diff(eval(input_styled))
    except Exception as e:
        print(e)
        print(input_styled)
        expected = "WRONG FORMAT!"
    actual = get_java_output(input=input).replace("^", "**")

    print('exp:', expected)
    print('act:', actual)
    if expected == "WRONG FORMAT!":
        assert actual == expected
        return

    if actual == "WRONG FORMAT!":
        # assert re.search(r'[-+][\ \t]*[-+][\ \t]+[0-9]', input) is not None or '\f' in input or '\\' in input
        print("input:", input)
        print('actual is WF, but py expected', expected)
        return

    actual = eval(actual)

    for i in range(20):
        if not isinstance(expected, int):
            expected = expected.subs(x, i)
        if not isinstance(actual, int):
            actual = actual.subs(x, i)
        assert expected == actual


def sample_generator(num_sample=None):
    yield ""
    yield "\n"
    yield "123*x\f"
    yield "123*x\v"
    yield "x"
    yield "+x-x"
    # yield '+x^- 1'
    yield '+ x'
    yield '123x'
    yield 'x^ 123 123*x'
    yield "123\f*x"
    yield "          "
    # yield "123\n+x"

    def assertExpDerEquals(i, p):
        return i

    def assertExpWrongFormat(i):
        return i
    yield "- +1"+"0"*1000
    yield assertExpDerEquals("0", "0")
    yield assertExpDerEquals("1", "0")
    yield assertExpDerEquals("x", "1")
    yield assertExpDerEquals("+x", "1")
    yield assertExpDerEquals("-x", "-1")
    yield assertExpDerEquals("234*x", "234")
    yield assertExpDerEquals("++123*x", "123")
    yield assertExpDerEquals("-  +123*x", "-123")
    yield assertExpWrongFormat("- + 123*x")
    yield assertExpDerEquals("4*x+x^2+x", "2*x+5")
    yield assertExpDerEquals("4*x+x^2+x", "4+2*x+1")
    yield assertExpWrongFormat("4x+x^2+x")
    yield assertExpDerEquals("- -4*x + x ^ 2 + x", "2*x+5")
    yield assertExpDerEquals("+4*x - -x^2 + x", "2*x+5")
    yield assertExpDerEquals("+19260817*x", "19260817")
    yield assertExpDerEquals("+ 19260817*x", "19260817")
    yield assertExpDerEquals("+ +19260817*x", "19260817")
    yield assertExpWrongFormat("++ 19260817*x")
    yield assertExpWrongFormat("1926 0817 * x")
    yield assertExpWrongFormat("")
    yield assertExpDerEquals("1234322340987832+x+     1234322340987832-x -           -78978678978977687      -x-x", "-2")
    #yield assertExpDerEquals("+0000050*x^000012", "--00600*x^+00011")
    #yield assertExpWrongFormat("0x123*x")
    yield assertExpWrongFormat("123F*x")
    yield assertExpWrongFormat("123f*x")
    yield assertExpWrongFormat("123o*x")
    #yield assertExpWrongFormat("12_3*x")
    yield assertExpWrongFormat("1*a^2")
    #yield assertExpWrongFormat("1.1*x^23")
    #yield assertExpWrongFormat("1/2*x")
    yield assertExpDerEquals("++ x+ - x ", "0")
    yield assertExpWrongFormat("1 233*x")
    #yield assertExpWrongFormat("12*x^+ 12")
    yield assertExpWrongFormat("12*x^ +1 2")
    #yield assertExpWrongFormat("2*x^ - 1")
    yield assertExpDerEquals("+ 1*x^12 ", "12*x^11")
    yield assertExpDerEquals("+\t1\t*x\t^12\t", "12*x^11")
    yield assertExpDerEquals("+\t 1 \t*x \t \t^12\t", "12*x^11")
    yield assertExpWrongFormat("\f1")
    yield assertExpWrongFormat("++ 1*x^12")
    yield assertExpWrongFormat("        xx^2")
    yield assertExpWrongFormat("x^^2")
    #yield assertExpWrongFormat("++-x^2")
    #yield assertExpWrongFormat("x^++2")
    yield assertExpWrongFormat("1x^3")
    #yield assertExpWrongFormat("1*x^+x")
    # yield assertExpWrongFormat("*x")
    yield assertExpDerEquals("-  4*  x^  0-x  ^  -529920192",
                             "529920192*x^-529920193")
    yield assertExpDerEquals("0*x+3", "0")
    yield assertExpDerEquals("0 * x^1 + x^0 + 4 * x ^ 2 - -5*x", "8*x+5")
    #yield assertExpDerEquals("5*x+6*x^1-x^1+00000*x^0+000010*x^+1",
    #                         "5+6-1+10")
    #yield assertExpDerEquals("4 * x ^-3 + 2 * x ^ -1 + 01 * x ^ 0 - 1 * x ^ 1",
    #                         "-12*x^-4+-2*x^-2+0-1")

    blank_pool = (['\t', ' ', '    ', '\t ', '', '', '']) * \
        1 + ['+']

    cnt = 0
    while num_sample is None or cnt < num_sample:
        cnt += 1
        sample = 1
        for _ in range(15):
            sample += random.choice([21283, -1439, 0])
            sample *= random.choice([47443, -23232, 1, x, sin(x), cos(x)])
        sample = str(sample.expand() if not isinstance(sample, int)
                     else sample).replace(' ', '').replace('**', '^')

        i = 0
        while i < len(sample):
            if sample[i] in ['+', '-']:
                chosen = random.choice(['+', '-', ''])
                sample = sample[:i] + chosen + sample[i:]
                if not chosen == '':
                    i += 1
            i += 1

        signs = ['+', '-', '*', '^']
        l = len(sample)
        for i in range(l):
            if i+1 < l and (sample[i+1] in signs) or (sample[i] in signs):
                sample = sample[:i+1] + \
                    random.choice(blank_pool) + sample[i+1:]

        ind = 3
        while True:
            suf = '+x^'+str(ind)
            if len(sample)+len(suf) > 1000:
                break
            sample = sample+suf
            ind += 1
        yield sample


def main():
    for i, sample in enumerate(sample_generator(800)):
        print('sample', i)
        assert_equals(sample)


if __name__ == '__main__':
    main()
