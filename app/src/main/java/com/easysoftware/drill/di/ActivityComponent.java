package com.easysoftware.drill.di;

import com.easysoftware.drill.ui.recognition.idiom.IdiomRecognitionActivity;
import com.easysoftware.drill.ui.recognition.poem5.Poem5RecognitionActivity;
import com.easysoftware.drill.ui.recognition.poem7.Poem7RecognitionActivity;
import com.easysoftware.drill.ui.solitaire.headandtail.idiom.IdiomSolitaireActivity;
import com.easysoftware.drill.ui.solitaire.headandtail.poem.PoemSolitaireActivity;

import dagger.Subcomponent;

@PerActivity
@Subcomponent (modules={ActivityModule.class})
public interface ActivityComponent {
    void inject(IdiomRecognitionActivity activity);
    void inject(Poem5RecognitionActivity activity);
    void inject(Poem7RecognitionActivity activity);

    void inject(IdiomSolitaireActivity activity);
    void inject(PoemSolitaireActivity activity);
}
