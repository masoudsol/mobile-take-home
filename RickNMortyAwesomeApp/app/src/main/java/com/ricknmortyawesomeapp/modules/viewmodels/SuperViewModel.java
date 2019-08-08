package com.ricknmortyawesomeapp.modules.viewmodels;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import com.ricknmortyawesomeapp.modules.repositories.RickRepository;
import com.ricknmortyawesomeapp.services.APIServices;

public class SuperViewModel extends AndroidViewModel {
    RickRepository dataProvider = RickRepository.getInstance();
    APIServices apiServices;

    public SuperViewModel(@NonNull Application application) {
        super(application);
        apiServices = new APIServices(application);

    }

    public void getImage(final String url, final EpisodeViewModel.ImageDownloadListener imageDownloadListener){
        Bitmap bitmap =  dataProvider.getImage( url);

        if (bitmap == null) {
            apiServices.fetchPhoto(url, new APIServices.CompletionListener() {
                @Override
                public void onCompletion(Boolean success, Exception error) {
                    if (success) {
                        imageDownloadListener.onEvent(dataProvider.getImage(url), null);
                    } else {
                        imageDownloadListener.onEvent(null, error);
                    }
                }
            });
        } else
            imageDownloadListener.onEvent(bitmap,null);
    }
}
