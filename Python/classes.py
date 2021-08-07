import re


class Name:
    name_rexp: str = r"^([a-zA-Z'-]{2,50})$"

    def __init__(self, given_name):
        self.value = self.validate(given_name)

    def validate(self, given: str):
        result = re.match(self.name_rexp, string=given)
        if result is None:
            raise ValueError
        else:
            return result.group(1)


class FourByteSignedInt:
    num_rexp = r"^-?([\d,]{1,14})$"

    four_byte_min = -2_147_483_648
    four_byte_max = 2_147_483_647

    def __init__(self, given_value):
        if (
            int(given_value) < self.four_byte_min
            or int(given_value) > self.four_byte_max
        ):
            raise ValueError
        else:
            self.value = self.validate(given_value)

    def validate(self, given: str):
        result = re.match(pattern=self.num_rexp, string=given)
        if result is None:
            raise ValueError
        else:
            return int(result.group(1))

    def __str__(self):
        return f"FBSI: {self.value}"


def strong_password(given: str) -> bool:
    """
    Checks a string for various features that strengthen a password.
    Regex created by Kittera McCloud
    """

    primary_exp: str = r"^([a-zA-Z0-9@#$%^&*_+-=/'?!;,.]{10,64})$"
    upper_case_exp: str = r"[A-Z]+"
    lower_case_exp: str = r"[a-z]+"
    one_digit_exp: str = r"\d+"
    one_punct_exp: str = r"[.,;'?!]"
    consecutive_lower_exp: str = r"[a-z]{3,}"

    candidate = re.match(pattern=primary_exp, string=given)

    def minimums_met(match):
        runner_up: str = match.group(1)
        one_upper_case: bool = (
            re.search(pattern=upper_case_exp, string=runner_up) is not None
        )
        one_lower_case: bool = (
            re.search(pattern=lower_case_exp, string=runner_up) is not None
        )
        one_digit: bool = re.search(pattern=one_digit_exp, string=runner_up) is not None
        one_punct: bool = re.search(pattern=one_punct_exp, string=runner_up) is not None
        no_consecutive_lowers: bool = (
            re.search(pattern=consecutive_lower_exp, string=runner_up) is None
        )

        return (
            one_upper_case
            and one_lower_case
            and one_digit
            and one_punct
            and no_consecutive_lowers
        )

    return False if candidate is None else minimums_met(candidate)
# S1xTyN!n3420


class Password:
    def __init__(self, given_word, given_salt):
        from hashlib import scrypt

        if strong_password(given_word):
            self.hashed = scrypt(
                password=bytearray(given_word, encoding="utf-8"),
                salt=bytearray(given_salt, encoding="utf-8"),
                n=8192,
                r=8,
                p=2,
            )
        else:
            raise ValueError
