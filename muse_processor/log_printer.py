#!/usr/bin/python3

import sys
from typing import Dict, List
from collections import Counter
class_method_dictionary_counter: Dict[str, int] = {}


def process_log(logs: str):
    sorted_leaks = []
    for log in logs.split("\n"):
        if "leak-" in log:
            sorted_leaks.append(log)

    sorted_leaks.sort()
    for s in sorted_leaks:
        print(s)


def preprocess_operator_placement(operator_placement: str):

    if "<" in operator_placement:
        return operator_placement[1:-1]
    else:
        return operator_placement


def process_log_for_class_and_method(mutation_log: str, tool_log: str):
    mutation_operators_only: List[str] = []
    for log in mutation_log.split("\n"):
        if "leak-" in log:
            operator_placement = log.split(": ")[1]
            operator_placement = preprocess_operator_placement(
                operator_placement)
            # print(operator_placement)
            mutation_operators_only.append(operator_placement)
        else:
            if "In file: " in log:
                # file_name = log.split("In file: ")[1]
                # mutation_operators_only.append("File,"+file_name)
                mutation_operators_only.append(log)
            # print(log)
    counted_operators: Dict[str, int] = Counter(mutation_operators_only)
    not_found: int = 0
    print("ClassName.Method,OperatorsInserted,FoundByTool,Difference,TypeOfDiffernce")
    for operator in counted_operators.keys():
        operator_count = counted_operators[operator]
        tool_log_count = tool_log.count(operator)
        if operator_count != tool_log_count:
            not_found += 1
            difference = str(operator_count - tool_log_count)
            diff_type = "negative" if (
                operator_count - tool_log_count) < 0 else "non_negative"
            if "In file: " in operator:
                print(operator.replace(": ", ","))
            else:
                print(operator+","+str(operator_count) +
                      ","+str(tool_log_count)+","+difference+","+diff_type)


if len(sys.argv) is 2:
    filename = sys.argv[1]
    from os import path

    if path.exists(filename):
        process_log(open(filename, "r").read())

if len(sys.argv) is 3:
    mutation_log = sys.argv[1]
    tool_log: str = sys.argv[2]

    from os import path

    if path.exists(mutation_log) and path.exists(tool_log):
        process_log_for_class_and_method(
            open(mutation_log, "r").read(),
            open(tool_log, "r").read(),
        )
