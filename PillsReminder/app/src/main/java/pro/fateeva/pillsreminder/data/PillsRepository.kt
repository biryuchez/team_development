package pro.fateeva.pillsreminder.data

import com.google.gson.GsonBuilder
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import pro.fateeva.pillsreminder.domain.entity.DrugDomain
import pro.fateeva.pillsreminder.domain.usecase.SearchPillsUsecase
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_API_URL = "https://app.rlsnet.ru/api/"
private const val PILL_TYPE_IDENTIFIER = "tn"

class PillsRepository : SearchPillsUsecase {
    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_API_URL)
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setLenient().create()))
            .client(RetrofitPillsApiClient().createClient())
            .build().create(PillsApi::class.java)
    }

    override suspend fun searchPills(query: String): List<DrugDomain> {
        return retrofit.searchPillsAsync(query)
            .await()
            .filter { it.type == PILL_TYPE_IDENTIFIER }
            .map {
                DrugDomain(it.id.toString(), it.name)
            }
    }
}