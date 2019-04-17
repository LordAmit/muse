# used to find only the executed logs from crashscope and list only the unique ones.
import sys
from typing import List, Set


def process_file(filename: str):
    print("filename: " + filename)
    leak_set: Set[str] = set()
    # with open(filename)
    lines: List[str] = open(filename).readlines()
    for line in lines:
        if "leak-" in line.lower():
            leak_substr: str = line.split("D/leak-")[1].split("(")[0].strip()
            leak_set.add(leak_substr)
    for item in leak_set:
        print(item)


if len(sys.argv) == 2:

    process_file(sys.argv[1])