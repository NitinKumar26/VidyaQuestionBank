package in.completecourse.questionbank.helper;

import in.completecourse.questionbank.model.Updates;
import retrofit2.Call;
import retrofit2.http.GET;


public interface APIService {

    @GET("updates")
    Call<Updates> getHeroes();
}
