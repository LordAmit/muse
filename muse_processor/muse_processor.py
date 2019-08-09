#!/usr/bin/python3
from shutil import copyfile
from pathlib import Path

from typing import Tuple, List
import os

sh_file_paths: List[str] = []
mutation_folder_paths: List[str] = []
path_to_muse_jar: str = "/home/amit/workspaces/muse/Muse.jar"
output_dir = "/home/amit/muse/output/"
mutation_types =\
    "SINK,REACHABILITY,TAINTSINK,COMPLEXREACHABILITY".split(
        ",")

input_folders = open("input_folders", mode='r').readlines()
name_inputFolders: List[Tuple] = []

for input_folder in input_folders:
    if "//" in input_folder:
        continue
    name, folder = input_folder.split(",")
    name_inputFolders.append((name.strip(), folder.strip()))
print(name_inputFolders)

for mutation_type in mutation_types:
    print(mutation_type)

#
# java -jar Muse.jar /home/amit/workspaces/muse/libs4ast /home/amit/muse/Tools/artifacts/activitylauncher-reachability/activitylauncher/ activity /home/amit/workspaces/muse/new_muse_output/TAINTSINK/ TAINTSINK
#  > activity_TAINTSINK.log

template_schema_apply = """#!/bin/sh
java -jar Muse.jar /home/amit/workspaces/muse/libs4ast {} {} {}{} {} > {}_{}.log
"""


def make_sh_file_with_x_bit(sh_file_path: str):
    if not Path(sh_file_path).exists():
        Path(sh_file_path).touch()
    os.chmod(sh_file_path, 0o755)


def create_dir(path: str):
    if not os.path.exists(path):
        os.makedirs(path)


def make_folder_structure(project_name: str):

    global sh_file_paths
    global mutation_folder_paths
    create_dir(output_dir + project_name)
    create_dir(output_dir + project_name + "/sh_files")
    create_dir(output_dir + project_name + "/schemas")

    for mutation_type in mutation_types:
        create_dir(output_dir + project_name + "/schemas/" + mutation_type)


def content_in_sh_per_schema(project: str, project_name: str, output_dir: str,
                             mutation: str):

    template_schema_apply = """#!/bin/sh
java -jar {} /home/amit/workspaces/muse/libs4ast {} {} {}{} {} > {}_{}.log
echo finished executing {}
    """
    return template_schema_apply.format(
        path_to_muse_jar, project, project_name,
        output_dir + project_name + "/schemas/", mutation, mutation,
        project_name, mutation, mutation)


def content_in_all_schema_sh_file(project_name: str, project_path: str,
                                  output_dir: str):
    sh_file_path = output_dir+project_name + \
        "/sh_files/"+"all_schema"+".sh"
    make_sh_file_with_x_bit(sh_file_path)
    sh_clean_project_folder(project_path)
    temp_string: str = "sh clean_project_folder.sh\n"
    for mutation in mutation_types:
        temp_string += "echo " + project_name + " " + mutation + "\n"
        temp_string += "sh " + project_name + "_" + mutation + ".sh\n"
    open(sh_file_path, mode="w").write(temp_string)


def chmod_x_all_gradlew_file_script(project_name: str, output_dir: str):
    sh_file_path = output_dir+project_name + \
        "/sh_files/"+"gradle_permission"+".sh"
    make_sh_file_with_x_bit(sh_file_path)
    temp_string: str = "chmod +x {}\n"
    content: str = "#!/bin/bash\n"
    for mutation in mutation_types:
        path_gradlew = output_dir+project_name + \
            "/schemas/"+mutation+"/"+project_name+"/gradlew"
        content += temp_string.format(path_gradlew)
    open(sh_file_path, mode="w").write(content)


def sh_clean_project_folder(project_path: str):
    sh_file_path = output_dir+project_name + \
        "/sh_files/"+"clean_project_folder"+".sh"
    make_sh_file_with_x_bit(sh_file_path)
    project_gradlew_path = project_path + "gradlew"
    os.chmod(project_gradlew_path, 0o755)
    temp_string = "#!/bin/bash\ncd {}\n{}gradlew clean".format(
        project_path, project_path)
    open(sh_file_path, mode="w").write(temp_string)


def clean_all(project_name: str, output_dir: str):
    # sh /home/amit/muse/output/android-timetracker/schemas/TAINTSINK/android-timetracker/gradlew build
    sh_file_path = output_dir+project_name + \
        "/sh_files/"+"clean_all"+".sh"
    make_sh_file_with_x_bit(sh_file_path)
    temp_string: str = "cd {}\n"
    temp_string += "{} clean\n"
    # temp_string += "{} assembleDebug\n"
    content: str = ""
    for mutation in mutation_types:
        path_project = output_dir+project_name + \
            "/schemas/"+mutation+"/"+project_name+"/"
        path_gradlew = output_dir+project_name + \
            "/schemas/"+mutation+"/"+project_name+"/gradlew"
        # if not os.path.exists(path_gradlew):
        #     print("error: gradlew not found at: "+path_gradlew)
        #     exit()
        content += temp_string.format(path_project, path_gradlew)
        # os.chmod(path_gradlew, 0o755)
    open(sh_file_path, mode="w").write(content)


def clean_and_build_all(project_name: str, output_dir: str):
    # sh /home/amit/muse/output/android-timetracker/schemas/TAINTSINK/android-timetracker/gradlew build
    sh_file_path = output_dir+project_name + \
        "/sh_files/"+"clean_and_build_all"+".sh"
    make_sh_file_with_x_bit(sh_file_path)
    temp_string: str = "cd {}\n"
    temp_string += "{} clean\n"
    temp_string += "{} assembleDebug\n"
    content: str = ""
    for mutation in mutation_types:
        path_project = output_dir+project_name + \
            "/schemas/"+mutation+"/"+project_name+"/"
        path_gradlew = output_dir+project_name + \
            "/schemas/"+mutation+"/"+project_name+"/gradlew"
        # if not os.path.exists(path_gradlew):
        #     print("error: gradlew not found at: "+path_gradlew)
        #     exit()
        content += temp_string.format(path_project, path_gradlew, path_gradlew)
        # os.chmod(path_gradlew, 0o755)
    open(sh_file_path, mode="w").write(content)


if __name__ == "__main__":
    for project_name, project_path in name_inputFolders:
        make_folder_structure(project_name)
        for mutation in mutation_types:
            output = content_in_sh_per_schema(project_path, project_name,
                                              output_dir, mutation)
            sh_file_path = output_dir+project_name + \
                "/sh_files/"+project_name+"_"+mutation+".sh"

            # print(output)
            make_sh_file_with_x_bit(sh_file_path)
            open(sh_file_path, mode="w").write(output)
        content_in_all_schema_sh_file(project_name, project_path, output_dir)
        # print("check1")
        clean_and_build_all(project_name, output_dir)
        clean_all(project_name, output_dir)
        # print("check2")
        chmod_x_all_gradlew_file_script(project_name, output_dir)
        # print("check3")
