import base64
import tensorflow as tf
import numpy as np
import cv2 as cv
import plotly.graph_objs as go
import plotly.express as px
import dash_core_components as dcc
import dash_html_components as html
from dash.dependencies import Input, Output, State

from PIL import Image
from app import app
from app import server
from io import BytesIO as _BytesIO

app.layout = html.Div([html.Div([
    dcc.Upload(
        id='upload-image',
        children=html.Div([
            'Drag and Drop or ',
            html.A('Select Files')
        ]),
        style={
            'width': '100%',
            'height': '60px',
            'lineHeight': '60px',
            'borderWidth': '1px',
            'borderStyle': 'dashed',
            'borderRadius': '5px',
            'textAlign': 'center',
            'margin': '10px'
        },
        # Allow multiple files to be uploaded
        multiple=False
    ),
    html.Div(id='output-image-upload')
]),
    html.Div(id='results')
])


def b64_to_pil(string):
    decoded = base64.b64decode(string)
    buffer = _BytesIO(decoded)
    im = Image.open(buffer)
    rgb = Image.new('RGB', im.size)
    rgb.paste(im)
    image = rgb
    test_image = image.resize((96, 96))

    return test_image


def b64_to_numpy(string, to_scalar=True):
    im = b64_to_pil(string)
    np_array = np.asarray(im)

    if to_scalar:
        np_array = np_array / 255.

    return np_array


def parse_contents(contents, filename):
    return html.Div([
        html.H5(filename),
        # HTML images accept base64 encoded strings in the same format
        # that is supplied by the upload
        html.Img(src=contents)
    ])


def parse_image(contents):
    classes_ = ['Apple', 'Apple scab', 'Bacterial Blight (CBB)', 'Bacterial spot',
                'Black rot', 'Brown Streak Disease (CBSD)', 'BrownSpot', 'Cassava',
                'Cedar apple rust', 'Cercospora leaf spot Gray leaf spot',
                'Cherry (including sour)', 'Common rust', 'Corn (maize)',
                'Early blight', 'Esca (Black Measles)', 'Grape',
                'Green Mottle (CGM)', 'Haunglongbing (Citrus greening)', 'Healthy',
                'Hispa', 'Late blight', 'Leaf Mold',
                'Leaf blight (Isariopsis Leaf Spot)', 'Leaf scorch', 'LeafBlast',
                'Mosaic Disease (CMD)', 'Northern Leaf Blight', 'Orange', 'Peach',
                'Pepper, bell', 'Potato', 'Powdery mildew', 'Rice',
                'Septoria leaf spot', 'Spider mites Two-spotted spider mite',
                'Squash', 'Strawberry', 'Target Spot', 'Tomato',
                'Tomato Yellow Leaf Curl Virus', 'Tomato mosaic virus', 'healthy']
    model = tf.keras.models.load_model('model/best_net_82.hdf5')
    content_type, content_string = contents.split(",")
    image = b64_to_numpy(content_string)
    image = image.reshape((1, 96, 96, 3))
    pred = model.predict(image)
    p = zip(list(classes_), list(pred[0]))
    p = sorted(list(p), key=lambda z: z[1], reverse=True)[:4]
    print(f'{p[0][0]}: {round(p[0][1] * 100, 2)}% \n {p[1][0]}: {round(p[1][1] * 100, 2)}%')

    return html.Div([html.P(
        f'{p[0][0]}: {round(p[0][1] * 100, 2)}%\n'
        f'{p[1][0]}: {round(p[1][1] * 100, 2)}%\n'
        f'{p[2][0]}: {round(p[2][1] * 100, 2)}%\n'
        f'{p[3][0]}: {round(p[3][1] * 100, 2)}%\n')])


@app.callback([Output('output-image-upload', 'children'), Output('results', 'children')],
              Input('upload-image', 'contents'),
              State('upload-image', 'filename'))
def update_output(list_of_contents, list_of_names):
    if list_of_contents is not None:
        return [[parse_contents(list_of_contents, list_of_names)], [parse_image(list_of_contents)]]


if __name__ == '__main__':
    app.run_server(debug=False)
