package IService;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;
import com.example.koimanagement.Models.Request.RegisterRequest;
import com.example.koimanagement.Models.Response.RegisterResponse;

public interface  IAuthApiService {
    @POST("Authen/Register")
    Call<RegisterResponse> register(@Body RegisterRequest request);
}
