import cv2
import mediapipe as mp

class ProcessImage:
    """
    This class is a class that is going to display image read by CV2

    Instance Variables:
        image_object
    """

    def __init__(self, image_name):
        self.image_name = image_name
        self.current_image = self.read_image(image_name)

    def read_image(self, image_name):
        self.current_image = cv2.imread(image_name)
        return self.current_image

    def __str__(self):
        return "This class reads an image and display it"

    def __repr__(self):
        return f"Image Info: shape={self.current_image.shape}"


my_pict = ProcessImage("hand-example.jpg")
print(my_pict)
print(repr(my_pict))
cv2.imshow("Image",my_pict.current_image)
cv2.waitKey(0)
cv2.destroyAllWindows()

