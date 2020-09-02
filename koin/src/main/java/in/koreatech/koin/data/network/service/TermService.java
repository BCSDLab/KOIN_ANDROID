package in.koreatech.koin.data.network.service;


import in.koreatech.koin.data.network.entity.Term;
import io.reactivex.Observable;
import retrofit2.http.GET;

import static in.koreatech.koin.constant.URLConstant.TERM;

public interface TermService {
    @GET(TERM)
    Observable<Term> getTerm();
}
