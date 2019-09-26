import logging

import logging
logging.basicConfig(
    level=logging.DEBUG, format=' %(asctime)s - %(levelname)s - %(message)s')

# logging.disable(logging.CRITICAL)


def log(s: str):
    logging.debug(s)


def disable():
    logging.disable(logging.CRITICAL)