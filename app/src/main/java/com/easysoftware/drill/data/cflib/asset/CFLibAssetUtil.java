package com.easysoftware.drill.data.cflib.asset;

import android.content.Context;

import com.easysoftware.drill.data.model.ChineseFragment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class CFLibAssetUtil {

    public static List<ChineseFragment> load(final InputStream is) throws IOException {
        List<ChineseFragment> cfList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new InputStreamReader(is,"UTF-8"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(" ");
                cfList.add(new ChineseFragment(parts[0]));
            }
        }

        return cfList;
    }

}
