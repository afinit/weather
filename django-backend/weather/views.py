from django.shortcuts import render

from rest_framework.views import APIView
from django.http.response import JsonResponse


import requests
import os

# Create your views here.

class GetWeather(APIView):
    """
    View to get weather for a specific latlong location
    """

    api_key = os.environ.get('OPENWEATHERMAP_API_KEY')

    def get(self, request, format=None):
        """
        return a weather json from the openweathermap api
        """

        latlong = request.GET.get('latlong')
        latlong_splits = latlong.split(',')

        if len(latlong_splits) != 2:
            return JsonResponse({'error': 'Invalid input'}, status=400)
        
        lat = latlong_splits[0]
        lon = latlong_splits[1]

        api_url = 'http://api.openweathermap.org/data/2.5/weather?lat={}&lon={}&units=imperial&appid={}'.format(lat, lon, self.api_key)
        response = requests.get(api_url)

        # Prepare the response to return to the user
        content = response.json()  # Get the content of the response
        status_code = response.status_code  # Get the HTTP status code

        # Return the response to the user
        return JsonResponse(content, status=status_code, headers={})