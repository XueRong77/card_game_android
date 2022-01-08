package iss.workshop.android_game_t3;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class SelectImgListener implements AdapterView.OnItemClickListener, View.OnClickListener {
    private final AppCompatActivity currentActivity;
    private final Bitmap selectedBitmap;
    private List<ImageDTO> fetchedImages; // change List<String> to store file's path/file name
    private List<Boolean> selectedFlags;
    private boolean downloadFinished;

    public SelectImgListener(AppCompatActivity currentActivity, Bitmap selectedBitmap) {
        this.currentActivity = currentActivity;
        this.selectedBitmap = selectedBitmap;
        this.downloadFinished = false;
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
            List<ImageDTO> selectedImages = IntStream.range(0, selectedFlags.size())
                    .filter(selectedFlags::get)
                    .mapToObj(fetchedImages::get)
                    .collect(Collectors.toList());

            System.out.println("jump to game activity...");
            Intent intent = new Intent(this.currentActivity, PlayActivity.class);
            intent.putExtra("images", (Serializable) selectedImages);
            this.currentActivity.startActivity(intent);
        }
    }

    @Override
    public void onClick(View v) {
//        List<ImageDTO> selectedImages = IntStream.range(0, selectedFlags.size())
//                .filter(selectedFlags::get)
//                .mapToObj(fetchedImages::get)
//                .collect(Collectors.toList());
        List<ImageDTO> selectedImages = new ArrayList<>();
        selectedImages.add(new ImageDTO(R.drawable.laugh, BitmapFactory.decodeResource(this.currentActivity.getResources(), R.drawable.laugh)));
        selectedImages.add(new ImageDTO(R.drawable.peep, BitmapFactory.decodeResource(this.currentActivity.getResources(), R.drawable.peep)));

        selectedImages.add(new ImageDTO(R.drawable.snore, BitmapFactory.decodeResource(this.currentActivity.getResources(), R.drawable.snore)));
        selectedImages.add(new ImageDTO(R.drawable.what, BitmapFactory.decodeResource(this.currentActivity.getResources(), R.drawable.what)));

        selectedImages.add(new ImageDTO(R.drawable.tired, BitmapFactory.decodeResource(this.currentActivity.getResources(), R.drawable.tired)));
        selectedImages.add(new ImageDTO(R.drawable.stop, BitmapFactory.decodeResource(this.currentActivity.getResources(), R.drawable.stop)));

        System.out.println("jump to game activity...");
        Intent intent = new Intent(this.currentActivity, PlayActivity.class);
        ArrayList<Bitmap> bitmaps = selectedImages
                .stream()
                .map(ImageDTO::getBitmap)
                .collect(Collectors.toCollection(ArrayList::new));
        intent.putParcelableArrayListExtra("images", bitmaps);
        this.currentActivity.startActivity(intent);
    }
}
