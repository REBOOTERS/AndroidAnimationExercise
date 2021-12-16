package com.engineer.imitate.ui.fragments.di

import dagger.Component
import dagger.Module
import dagger.Provides
import javax.inject.Inject


@Component(modules = [NetworkModule::class])
interface ApplicationComponent {
    fun inject(diFragment: DIFragment)
}


class LoginViewModel @Inject constructor(
    private val userRepository: UserRepository
) {
    fun request(block: (String) -> Unit) {
        block(userRepository.getData())
    }
}

// @Module informs Dagger that this class is a Dagger Module
@Module
class NetworkModule {

    // @Provides tell Dagger how to create instances of the type that this function
    // returns (i.e. LoginRetrofitService).
    // Function parameters are the dependencies of this type.
    @Provides
    fun provideLoginRetrofitService(): LoginRetrofitService {
        // Whenever Dagger needs to provide an instance of type LoginRetrofitService,
        // this code (the one inside the @Provides method) is run.
//        return Retrofit.Builder()
//            .baseUrl("https://example.com")
//            .build()
//            .create(LoginService::class.java)

        return object : LoginService {
            override fun provideData(): String {
                return "I came from dagger"
            }

        }
    }
}
//
///************************************************************************************/

open interface LoginRetrofitService {
    fun provideData(): String
}

interface LoginService : LoginRetrofitService {

}

// @Inject lets Dagger know how to create instances of these objects
class UserLocalDataSource @Inject constructor() {}
class UserRemoteDataSource @Inject constructor(private val loginService: LoginRetrofitService) {
    fun getData(): String {
        return loginService.provideData()
    }
}


// @Inject lets Dagger know how to create instances of this object
class UserRepository @Inject constructor(
    private val localDataSource: UserLocalDataSource,
    private val remoteDataSource: UserRemoteDataSource
) {

    fun getData(): String {
        return remoteDataSource.getData()
    }
}
//
// @Component makes Dagger create a graph of dependencies
//@Component
//interface ApplicationGraph {
//    // The return type  of functions inside the component interface is
//    // what can be provided from the container
//    fun repository()
//}