package dk.aau.hr.pdbilpleje.Payment.Network;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MyApiService {
    @GET("Services/get_client_key.php")
    Observable<String> getClientKey(
            @Query("api") String api,
            @Query("amount") int amount
    );

}
