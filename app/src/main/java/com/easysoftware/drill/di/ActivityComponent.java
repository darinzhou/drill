package com.easysoftware.drill.di;

import com.easysoftware.drill.ui.main.MainActivity;
import com.easysoftware.drill.ui.recognition.idiom.IdiomRecognitionActivity;
import com.easysoftware.drill.ui.recognition.poem5.Poem5RecognitionActivity;
import com.easysoftware.drill.ui.recognition.poem7.Poem7RecognitionActivity;
import com.easysoftware.drill.ui.solitaire.keywordheadandtail.idiom.IdiomSolitaireWithKeywordHeadAndTailActivity;
import com.easysoftware.drill.ui.solitaire.keywordheadandtail.poem.PoemSolitaireWithKeywordHeadAndTailActivity;
import com.easysoftware.drill.ui.solitaire.keywordinside.poem.PoemSolitaireWithKeywordInsideActivity;

import dagger.Subcomponent;

@PerActivity
@Subcomponent (modules={ActivityModule.class})
public interface ActivityComponent {
    void inject(MainActivity activity);

    void inject(IdiomRecognitionActivity activity);
    void inject(Poem5RecognitionActivity activity);
    void inject(Poem7RecognitionActivity activity);

    void inject(IdiomSolitaireWithKeywordHeadAndTailActivity activity);
    void inject(PoemSolitaireWithKeywordHeadAndTailActivity activity);

    void inject(PoemSolitaireWithKeywordInsideActivity activity);
}
