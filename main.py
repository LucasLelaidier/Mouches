import cv2

from algorithm.TestAlgorithm import TestAlgorithm
from algorithm.Input import Input
from algorithm.Output import Output

algo = TestAlgorithm(Input("image.png"))
output = algo.run()

print(output.duration)