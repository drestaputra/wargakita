package dresta.putra.wargakita.chat.java.utils.imagepick;

import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import dresta.putra.wargakita.chat.java.utils.imagepick.fragment.MediaPickHelperFragment;
import dresta.putra.wargakita.chat.java.utils.imagepick.fragment.MediaSourcePickDialogFragment;

public class ImagePickHelper {

    public void pickAnImage(FragmentActivity activity, int requestCode) {
        MediaPickHelperFragment mediaPickHelperFragment = MediaPickHelperFragment.start(activity, requestCode);
        showImageSourcePickerDialog(activity.getSupportFragmentManager(), mediaPickHelperFragment);
    }

    private void showImageSourcePickerDialog(FragmentManager fm, MediaPickHelperFragment fragment) {
        MediaSourcePickDialogFragment.show(fm,
                new MediaSourcePickDialogFragment.ImageSourcePickedListener(fragment));
    }
}