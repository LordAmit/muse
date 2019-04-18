#!/usr/bin/env python

from argparse import ArgumentParser
import re

def parse_args() :
    parser = ArgumentParser()
    parser.add_argument("logcat", help="Android logcat file")
    parser.add_argument("tool", help="Tool report")
    return parser.parse_args()

def extract_leaks(file):
    leaks = set()
    for line in open(file, 'r'):
        match = re.search(r"(leak-[0-9]+)", line)
        if match:
            leaks.add(match.group(1))
    return leaks


def main():
    args = parse_args()
    leaks = extract_leaks(args.logcat)
    detected = extract_leaks(args.tool)
    combined = leaks.union(detected)
    res = [0, 0, 0]
    for leak in sorted(list(combined), key=lambda x: int(x.split('-')[1])):
        if leak not in detected:
            print("%s - UNSOUND" % (leak))
            res[0] += 1
        elif leak not in leaks:
            print("%s - IMPRECISE" % (leak))
            res[1] += 1
        else :
            print("%s - CORRECT" % (leak))
            res[2] += 1
    print("V&!D: %d" % (res[0]))
    print("!V&D: %d" % (res[1]))
    print("V&D: %d" % (res[2]))
    
if __name__ == '__main__':
    main()
