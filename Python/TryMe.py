import datetime

from classes import Name, FourByteSignedInt, Password
import os


def print_error(type: str, message):
    error_file = open("./problem_report.txt", encoding="utf-8", mode="a")
    stamp = datetime.datetime.now()
    error_file.write(f"{stamp} Problem type: {type}, Problem desc: {str(message)}\n")
    error_file.close()
    # print(f"{stamp} Problem type: {type}, Problem desc: {message}\n", file=errors_file)


def get_names() -> (str, str):
    print(
        "------------------------------------\n"
        "Step 1: Name\n"
        "Allowed Characters: a-z A-z ' -\n"
        "Character Count: 2..50"
    )

    first_name = "Alice"
    last_name = "Bob"

    loop = True
    while loop:
        try:
            first_name = Name(given_name=input(" First Name?: "))
            last_name = Name(given_name=input(" Last Name?: "))
            loop = False
            print("Now THIS is a name.")
        except ValueError as ve:
            print_error("ValueError", ve)
            print("That's not a name!")

    return first_name, last_name


def get_integers() -> (FourByteSignedInt, FourByteSignedInt):
    global error_list
    print(
        "------------------------------------\n"
        "Step 2: Four-Byte Numbers\n"
        "Allowed Characters: 0-9 , -\n"
        "Allowed Character Count: 1..14"
    )

    def flow_enforcement(val_1, val_2):
        valid_format = isinstance(val_1.value, int) and isinstance(val_2.value, int)
        if not valid_format:
            raise ValueError("Didn't find two integers.")

        a = int(val_1.value)
        b = int(val_2.value)

        valid_range = (
                FourByteSignedInt.four_byte_min <= a <= FourByteSignedInt.four_byte_max
                and FourByteSignedInt.four_byte_min <= b <= FourByteSignedInt.four_byte_max
        )

        if not valid_range:
            raise ValueError("Integers out of range.")

    loop = True
    num1 = FourByteSignedInt("0")
    num2 = FourByteSignedInt("1")

    while loop:
        try:
            num1 = FourByteSignedInt(input(" First Integer: "))
            num2 = FourByteSignedInt(input(" Second Integer: "))
            flow_enforcement(val_1=num1, val_2=num2)
            loop = False
            print("The math checks out!")
        except ValueError as ve:
            print_error("ValueError", ve)
            print("These are not the numbers we're looking for...")

    return int(num1.value), int(num2.value)


def safe_path(base, path):
    matchpath = os.path.realpath(path)
    return base == os.path.commonpath((base, matchpath))


def get_input_filename() -> str:
    print(
        "------------------------------------\n"
        "Step 3: Input File\n"
        "The input file may only be a txt file placed under the same directory as TryMe.py"
    )

    loop = True
    while loop:
        proposed_path = input("Input Filename: ")
        if safe_path(os.getcwd(), proposed_path) and proposed_path.endswith(".txt"):
            try:
                file = open(proposed_path, mode="r", errors="strict", encoding="utf-8")
                trash = file.read()
                del trash
                file.close()
                return proposed_path
            except OSError as ose:
                print_error("OSError", ose)
                print("File invalid.")
                loop = True
            except UnicodeDecodeError as ude:
                print_error("UnicodeDecodeError", "Found non-text bytes in input.")
                print("File invalid.")
                loop = True
            except ValueError as ve:
                print_error("ValueError", ve)
                print("Try again.")
                loop = True


def get_output_filename(in_file_name: str) -> str:
    print(
        "------------------------------------\n"
        "Step 4: Output File\n"
        "The output file may only be a txt file placed under the same directory as TryMe.py\n"
        "The output file name may not be the same as that of the input file."
    )
    loop = True
    while loop:
        proposed_path = input("Output Filename: ")
        if safe_path(os.getcwd(), proposed_path) and proposed_path.endswith(".txt"):
            if proposed_path == in_file_name:
                print_error("ValueError", "Cannot provide same path for input and output files.")
                print("Filename cannot match input.")
            else:
                try:
                    open(proposed_path, mode="a", errors="strict", encoding="utf-8").close()
                    return proposed_path
                except OSError as ose:
                    print_error("OSError", ose)
                    loop = True
                except ValueError as ve:
                    print_error("ValueError", ve)
                    loop = True


def get_password(salt):
    print(
        "------------------------------------\n"
        "Step 5: Password\n"
        "Please enter and confirm a password containing the following:\n"
        "- at least one capital letter\n"
        "- at least one lowercase letter\n"
        "- at least one base-ten digit\n"
        "- at least one punctuation mark\n"
        "- no more than three consecutive lowercase letters"
    )

    from random import randint

    temp_pwd = "./{0}.file".format(randint(100000, 999999999999999))

    loop = True
    while loop:
        try:
            phrase = Password(input(" Enter new password: "), given_salt=salt)
            with open(temp_pwd, mode="xb") as tmp:
                tmp.write(phrase.hashed)
            loop = False
        except ValueError as ve:
            print_error("ValueError", ve)
            print(ve)
            print("Invalid password.")
            try:
                os.remove(temp_pwd)
            except FileNotFoundError:
                pass
            temp_pwd = "./{0}.file".format(
                randint(10000000000000000, 99999999999999999)
            )

    return confirm_password(temp_pwd, salt)


def confirm_password(hash_file, salt):
    with open(hash_file, mode="rb") as prev_hash_file:
        prev_hash = prev_hash_file.read()

    os.remove(hash_file)

    try:
        phrase_two = Password(given_word=input(" Confirm Password: "), given_salt=salt, confirming=True)
        confirmed = phrase_two.hashed == prev_hash
    except ValueError as ve:
        print_error("ValueError", ve)
        confirmed = False

    return confirmed


def output_to_file(data):
    with open(data["output_path"], mode="a", encoding="utf-8") as out_file:
        out_file.write(f"\nAPPENDING OUTPUT ON {datetime.datetime.now()}\n---------------------\n")

        out_file.write(f"First Name: {data['first_name']}\n")
        out_file.write(f"Last Name: {data['last_name']}\n")
        out_file.write("\n")

        a = int(data['operand_1'])
        b = int(data['operand_2'])
        out_file.write(f"Result of adding integers given: {a + b}\n")
        out_file.write(f"Result of multiplying integers given: {a * b}\n")
        out_file.write("\n")

        out_file.write("Content of input file:\n")
        with open(data['input_path'], mode="r", encoding="utf-8") as in_file:
            out_file.writelines(in_file.readlines())

        out_file.write("\n\n\n")


def intro():
    print("Welcome to the Sandbox!\nLet's see what shenanigans we can get into...")


def main():
    from secrets import token_hex

    intro()
    f_name, l_name = get_names()
    num1, num2 = get_integers()
    input_file = get_input_filename()
    output_file = get_output_filename(input_file)

    passwords_bad = True
    while passwords_bad:
        salty_flavor = token_hex(64)
        passwords_bad = not get_password(salty_flavor)
        if passwords_bad:
            print("Didn't match.")
        else:
            print("Password confirmed.")

    output_to_file(
        {
            "first_name": f_name.value,
            "last_name": l_name.value,
            "operand_1": num1,
            "operand_2": num2,
            "input_path": input_file,
            "output_path": output_file,
        }
    )


main()
