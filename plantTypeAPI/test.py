import requests
import json
import os

curr_directory = os.path.dirname(os.path.abspath(__file__))
file_directory = os.path.join(curr_directory, "images.jpeg")

def upload_image(file_directory):
    imageID = None
    with open(file_directory, 'rb') as image_file:
        files = {
            'file': ('images.jpeg', image_file, 'image/jpeg'),  # File name, file object, and MIME type
        }

        headers = {
            'accept': '*/*',
            'accept-language': 'en-AU,en-US;q=0.9,en-GB;q=0.8,en;q=0.7',
            'cache-control': 'no-cache',
            'dnt': '1',
            'origin': 'https://identify.plantnet.org',
            'pragma': 'no-cache',
            'priority': 'u=1, i',
            'referer': 'https://identify.plantnet.org/',
            'sec-ch-ua': '"Google Chrome";v="129", "Not=A?Brand";v="8", "Chromium";v="129"',
            'sec-ch-ua-mobile': '?1',
            'sec-ch-ua-platform': '"Android"',
            'sec-fetch-dest': 'empty',
            'sec-fetch-mode': 'cors',
            'sec-fetch-site': 'same-site',
            'user-agent': 'Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/129.0.0.0 Mobile Safari/537.36',
        }

        # Send the POST request with the image
        response = json.loads(requests.post('https://api.plantnet.org/v1/images', headers=headers, files=files).text)
        imageID = response['id']
    return imageID

def get_type(imageID):
    headers = {
        'accept': 'application/json',
        'accept-language': 'en-AU,en-US;q=0.9,en-GB;q=0.8,en;q=0.7',
        'cache-control': 'no-cache',
        'content-type': 'application/json',
        'dnt': '1',
        'origin': 'https://identify.plantnet.org',
        'pragma': 'no-cache',
        'priority': 'u=1, i',
        'referer': 'https://identify.plantnet.org/',
        'sec-ch-ua': '"Google Chrome";v="129", "Not=A?Brand";v="8", "Chromium";v="129"',
        'sec-ch-ua-mobile': '?1',
        'sec-ch-ua-platform': '"Android"',
        'sec-fetch-dest': 'empty',
        'sec-fetch-mode': 'cors',
        'sec-fetch-site': 'same-site',
        'user-agent': 'Mozilla/5.0 (Linux; Android 6.0; Nexus 5 Build/MRA58N) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/129.0.0.0 Mobile Safari/537.36',
    }

    params = {
        'illustratedOnly': 'true',
        'clientType': 'web',
        'clientVersion': '3.0.0',
        'lang': 'en-au',
        'kt': 'true',
        'mediaSource': 'file',
    }

    json_data = {
        'images': [
            {
                'url': f'https://bs.plantnet.org/v1/image/o/{imageID}',
            },
        ],
    }

    response = requests.post(
        'https://api.plantnet.org/v1/projects/k-world-flora/queries/identify',
        params=params,
        headers=headers,
        json=json_data,
    )
    response = json.loads(response.text)
    return response

print(get_type(upload_image(file_directory)))

