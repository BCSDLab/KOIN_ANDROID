package in.koreatech.koin.data.network.entity;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Term {
    private final String Tag = Term.class.getSimpleName();

    @SerializedName("term")
    @Expose
    private final int term;

    public Term(int term) {
        this.term = term;
    }

    public int getTerm() {
        return term;
    }
}
