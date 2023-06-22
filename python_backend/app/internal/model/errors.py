class InvalidLatLongError(Exception):
    def __init__(self, value: str):
        self.value = value

    def __str__(self) -> str:
        return f"Invalid latitude longitude format. Expected format example: '38.624399,-90.184242'. Actual values supplied: '{self.value}'"
    
class InternalError(Exception):
    def __init__(self, value: str):
        self.value = value

    def __str__(self) -> str:
        return f"Internal error: {self.value}"
