package iss.workshop.android_game_t3;

import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SelectImgListener implements AdapterView.OnItemClickListener {
    private final AppCompatActivity currentActivity;
    private final Bitmap selectedBitmap;
    private List<File> files;
    private List<ImageDTO> fetchedImages;
    private List<Boolean> selectedFlags;
    private boolean downloadFinished;

    public SelectImgListener(AppCompatActivity currentActivity, Bitmap selectedBitmap) {
        this.currentActivity = currentActivity;
        this.selectedBitmap = selectedBitmap;
        this.downloadFinished = false;
    }

    public void setFiles(List<File> files) {
        this.files = files;
    }

    public void setFetchedImages(List<ImageDTO> fetchedImages) {
        this.fetchedImages = fetchedImages;
    }

    public void setSelectedFlags(List<Boolean> selectedFlags) {
        this.selectedFlags = selectedFlags;
    }

    public void setDownloadFinished(boolean downloadFinished) {
        this.downloadFinished = downloadFinished;
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
        if (!downloadFinished) {
            return;
        }

        GridView gridView = (GridView) adapterView;
        ViewGroup gridElement = (ViewGroup) gridView.getChildAt(position);

        Boolean selected = !selectedFlags.get(position);
        selectedFlags.set(position, selected);

        ImageView clickedView = (ImageView) gridElement.getChildAt(0);
        if (selected) {
            clickedView.setImageBitmap(selectedBitmap);
        } else {
            clickedView.setImageBitmap(fetchedImages.get(position).getBitmap());
        }

        int numOfSelected = Collections.frequency(selectedFlags, true);
        if (numOfSelected == 6) {
            ArrayList<String> filePaths = IntStream.range(0, selectedFlags.size())
                    .filter(selectedFlags::get)
                    .mapToObj(i -> {
                        File file = files.get(i);
                        return file.getAbsolutePath();
                    })
                    .collect(Collectors.toCollection(ArrayList::new));
            Intent intent = new Intent(this.currentActivity, PlayActivity.class);
            intent.putStringArrayListExtra("image_paths", filePaths);
            this.currentActivity.startActivity(intent);
        }
    }
}