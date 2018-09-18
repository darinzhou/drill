package com.easysoftware.drill.data.cflib;

import com.easysoftware.drill.data.model.ChineseFragment;

import java.io.IOException;
import java.util.List;

public interface CFLibraryLoader {
    List<ChineseFragment> load() throws IOException;
}
