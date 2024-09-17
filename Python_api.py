#Created By Chirag And OneKnown
from flask import Flask, request, jsonify
from googletrans import Translator

app = Flask(__name__)

# List of Indian language codes
indian_languages = [
    'hi',  # Hindi
    'bn',  # Bengali
    'te',  # Telugu
    'mr',  # Marathi
    'ta',  # Tamil
    'ur',  # Urdu
    'gu',  # Gujarati
    'ml',  # Malayalam
    'kn',  # Kannadapip install 
    'pa',  # Punjabi
    'as',  # Assamese
    'or'   # Oriya
]

translator = Translator()

@app.route('/translate', methods=['GET'])
def translate():
    text = request.args.get('text')
    dest_language = request.args.get('dest_language')

    if not text or not dest_language:
        return jsonify({'error': 'Text and destination language are required'}), 400

    if dest_language not in indian_languages:
        return jsonify({'error': 'Invalid destination language code'}), 400

    try:
        translation = translator.translate(text, dest=dest_language)
        return jsonify({'translated_text': translation.text})
    except Exception as e:
        return jsonify({'error': str(e)}), 500

if __name__ == '__main__':
    app.run(debug=True)
