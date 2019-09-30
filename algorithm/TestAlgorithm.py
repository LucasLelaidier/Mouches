from .Input import Input
from .Output import Output

class TestAlgorithm():

    def __init__(self, input):
        self.input = input
    
    def run(self):
        return Output("image.png", 500)
