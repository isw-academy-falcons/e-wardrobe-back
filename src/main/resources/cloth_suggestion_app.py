import cv2
import torch
import numpy as np
import torchvision.models as models
import torchvision.transforms as transforms
from scipy.spatial.distance import cosine
import argparse
import os  # Import the 'os' module for path manipulation

# Add command-line arguments to accept input data or configuration
parser = argparse.ArgumentParser(description='Clothing Suggestion Script')
parser.add_argument('--top_images_file', help='Path to a text file containing top clothing image paths')
parser.add_argument('--below_torso_images_file', help='Path to a text file containing bottom clothing image paths')
parser.add_argument('--dress_images_file', help='Path to a text file containing dress image paths')
args = parser.parse_args()

# Check if required input arguments are provided
if not args.top_images_file or not args.below_torso_images_file or not args.dress_images_file:
    print("Error: Please provide input image paths files using --top_images_file, --below_torso_images_file, and --dress_images_file")
    exit(1)

# Define a function to read image paths from a text file
def read_image_paths_from_file(file_path):
    with open(file_path, 'r') as file:
        image_paths = file.read().splitlines()
    return image_paths

# Define a function to preprocess an image
def preprocess_image(image_path):
    try:
        # Check if the image file exists
        if not os.path.exists(image_path):
            raise FileNotFoundError(f"Image file not found: {image_path}")

        # Load and preprocess the image
        image = cv2.imread(image_path)
        image = cv2.cvtColor(image, cv2.COLOR_BGR2RGB)
        image = transforms.Compose([transforms.ToPILImage(), transforms.Resize((224, 224)), transforms.ToTensor()])(image)
        image = image.unsqueeze(0)  # Add batch dimension
        return image
    except Exception as e:
        print(f"Error preprocessing image {image_path}: {str(e)}")
        return None  # Return None in case of error

# Define a function to perform clothing recommendation
def recommend_clothing():
    # Read image paths from the provided text files
    top_images = read_image_paths_from_file(args.top_images_file)
    below_torso_images = read_image_paths_from_file(args.below_torso_images_file)
    dress_images = read_image_paths_from_file(args.dress_images_file)

    if not top_images or not below_torso_images or not dress_images:
        print("Error: No image paths found in one or more of the provided text files.")
        return

    # Load and preprocess all clothing images (tops, bottoms, dresses)
    input_tensors1 = [preprocess_image(image_path) for image_path in top_images if preprocess_image(image_path) is not None]
    input_tensors2 = [preprocess_image(image_path) for image_path in below_torso_images if preprocess_image(image_path) is not None]
    input_tensors3 = [preprocess_image(image_path) for image_path in dress_images if preprocess_image(image_path) is not None]

    # Load the pre-trained ResNet model with 18 layers
    resnet18 = models.resnet18(pretrained=True)
    # Set the model to evaluation mode
    resnet18.eval()

    # Extract features from the pre-trained model for each clothing image
    with torch.no_grad():
        features_list1 = [resnet18(input_tensor).squeeze().numpy() for input_tensor in input_tensors1]
        features_list2 = [resnet18(input_tensor).squeeze().numpy() for input_tensor in input_tensors2]
        features_list3 = [resnet18(input_tensor).squeeze().numpy() for input_tensor in input_tensors3]

    # Perform clothing recommendation based on features and similarity metrics
    for i, feature1 in enumerate(features_list1):
        best_match = None
        best_similarity = -1.0

        for j, feature2 in enumerate(features_list2):
            similarity = calculate_similarity(feature1, feature2)
            if similarity > best_similarity:
                best_similarity = similarity
                best_match = below_torso_images[j]

        for k, feature3 in enumerate(features_list3):
            similarity = calculate_similarity(feature1, feature3)
            if similarity > best_similarity:
                best_similarity = similarity
                best_match = dress_images[k]

        # Print or return the recommended clothing pairs
        print(f"Top: {top_images[i]}")
        print(f"Recommended Clothing: {best_match}")
        print()

if __name__ == "__main__":
    recommend_clothing()
