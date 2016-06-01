# InstagramProfile
[![Build Status](https://travis-ci.org/hasanpour/InstagramProfile.svg?branch=master)](https://travis-ci.org/hasanpour/InstagramProfile)

It is a simple Android example that shows user's profile in list and grid mode.

## Features
- Authenticate users using [Instagram Server-Side (Explicit) OAuth flow](https://www.instagram.com/developer/authentication).
- Fetching profile info.
- Fetching user's images and show on a list and grid view.
- Fetching the caption and date of images.
- Using [recyclerView](https://developer.android.com/reference/android/support/v7/widget/RecyclerView.html) and [Picasso](http://square.github.io/picasso) for showing images.
- Using tabs with [fragment swipeable view](https://developer.android.com/training/implementing-navigation/lateral.html).

## Note
Before compiling the app make sure to put your client_id and client_secret on [strings.xml] (app/src/main/res/values/strings.xml).
For more information please visit [Instagram Developer Documentation](https://www.instagram.com/developer).

## License
See the [LICENSE](LICENSE.md) file for license rights and limitations (MIT).