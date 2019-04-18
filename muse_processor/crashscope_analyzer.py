# used to find only the executed logs from crashscope and list only the unique ones.
import sys
from typing import List, Set


def process_file(filename_crashscope: str, filename_leak_log: str):
    count_yes: int = 0
    count_no: int = 0
    whole_leak_count: int = 0
    print("filename: " + filename_crashscope)
    leak_set: Set[str] = set()
    # with open(filename)
    lines: List[str] = open(filename_crashscope).readlines()
    for line in lines:
        line = line.lower()
        if "leak-" in line:
            whole_leak_count += 1
            leak_substr: str = line.split("/leak-")[1].split("(")[0].strip()
            leak_set.add(leak_substr)
    contents = open(filename_leak_log).read()
    for item in leak_set:
        str_to_check = "leak-" + item
        if str_to_check.lower() in contents.lower():
            print(str_to_check + ": yes")
            count_yes += 1
        else:
            count_no += 1
            print(str_to_check + ": no")
    print("total leaks:", whole_leak_count, "unique leaks:", len(leak_set),
          "yes:", count_yes, "no:", count_no)


if len(sys.argv) == 3:
    process_file(sys.argv[1], sys.argv[2])
