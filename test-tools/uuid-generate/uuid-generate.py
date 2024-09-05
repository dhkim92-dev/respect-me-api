import time
import random
import uuid

class UUIDV7:
    def __init__(self):
        timestamp = int(time.time() * 1000)
        timestamp &= 0x0000FFFFFFFFFFFF

        random_bits = random.getrandbits(12)
        most_sig_bits = (timestamp << 16) | 0x7000 | random_bits
        random_bits = random.getrandbits(62)
        least_sig_bits = (0b10 << 62) | random_bits
        self.uuid = uuid.UUID(int=(most_sig_bits << 64) | least_sig_bits)

    def to_string(self):
        return str(self.uuid)

if __name__ == '__main__' :
    print(UUIDV7().to_string())