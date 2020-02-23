package in.koreatech.koin.core.networks.services;


import in.koreatech.koin.core.networks.entity.Term;
import io.reactivex.Observable;
import retrofit2.http.GET;

import static in.koreatech.koin.core.constants.URLConstant.TERM;

public interface TermService {
    @GET(TERM)
    Observable<Term> getTerm();
}
