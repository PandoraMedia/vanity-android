# Vanity

Vanity is a lightweight Android library to retrieve Instagram profile pictures.

## How to Use it

To use this library you most first register your application and obtain a client id. Those instructions can be found [here](https://www.instagram.com/developer/register/).

Once your application has been registered you can integrate this library with a few simple steps.

First create an `Activity` that extends `VanityActivity`:

    package some.application;

    public class MyVanityActivity extends com.wseemann.vanity.activity.VanityActivity { }

Next, call the activity you created with the correct intent extras:

    private static final int INSTAGRAM_PICTURE_REQUEST_CODE = 100;

    …

    Intent intent = new Intent(this, MyVanityActivity.class);
    intent.putExtra(VanityConstants.INTENT_EXTRA_CLIENT_ID, "Your Instagram client id");
    intent.putExtra(VanityConstants.INTENT_EXTRA_REDIRECT_URI, "Your Instagram callback uri");
    intent.putExtra(VanityConstants.INTENT_EXTRA_PICTURE_QUALITY, VanityConstants.HIGH_QUALITY); // optional, returns a higher quality picture

    startActivityForResult(intent, INSTAGRAM_PICTURE_REQUEST_CODE);

Finally, add code to process the result of calling the library:

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == INSTAGRAM_PICTURE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                VanityData vanityData = data.getParcelableExtra(VanityConstants.RESULT_EXTRA_VANITY_DATA);
                // Do something with the result. For example, create a bitmap…
                //Bitmap bitmap = BitmapFactory.decodeByteArray(vanityData.getPictureData(), 0, vanityData.getPictureData().length);
                //pictureImageView.setImageBitmap(bitmap);
            }
        }
    }

## Dependencies

Vanity works with AndroidX apps. It is built in Kotlin on top of Architecture Components and RxJava.

## Gradle 

```groovy
dependencies {
    implementation 'com.pandora.vanity:vanity:1.0'
}
```

## License
```
Copyright 2019 Pandora Media, LLC

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
See accompanying LICENSE file or you may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
