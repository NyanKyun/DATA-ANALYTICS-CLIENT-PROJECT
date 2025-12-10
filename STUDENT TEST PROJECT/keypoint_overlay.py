import cv2
import mediapipe as mp
from mediapipe.tasks import python
from mediapipe.tasks.python import vision
import numpy as np

class ProcessImage:
    def __init__(self, image_name, model_name):
        self.image_name = image_name
        self.model_name = model_name

        self.initialize_model()
        self.current_image = self.read_image(image_name)
        self.shape_information = self.current_image.shape
        
        self.result = self.detect_hands()
        self.draw_landmarks()

    def initialize_model(self):
        base = python.BaseOptions(model_asset_path=self.model_name)
        self.options = vision.HandLandmarkerOptions(
            base_options=base,
            num_hands=2
        )
        self.detector = vision.HandLandmarker.create_from_options(self.options)

    def read_image(self, path):
        img = cv2.imread(path)
        if img is None:
            raise FileNotFoundError(f"Image not found: {path}")
        return img  # BGR

    def detect_hands(self):
        # To RGB only here
        rgb_image = cv2.cvtColor(self.current_image, cv2.COLOR_BGR2RGB)

        mp_image = mp.Image(
            image_format=mp.ImageFormat.SRGB,
            data=rgb_image
        )

        result = self.detector.detect(mp_image)
        return result

    def draw_landmarks(self):
        annotated = self.current_image.copy()

        if self.result.hand_landmarks:
            for hand in self.result.hand_landmarks:
                for lm in hand:
                    x = int(lm.x * annotated.shape[1])
                    y = int(lm.y * annotated.shape[0])
                    cv2.circle(annotated, (x, y), 4, (0, 255, 0), -1)

        self.current_image = annotated 

    def __str__(self):
        return "This class reads an image and displays hand landmarks."

    def __repr__(self):
        return f"Image Info: shape={self.shape_information}"


# Run
my_pict = ProcessImage("hand-example.jpg", "hand_landmarker.task")

print(my_pict)
print(repr(my_pict))

cv2.imshow("Landmarks", my_pict.current_image)
cv2.waitKey(0)
cv2.destroyAllWindows()
