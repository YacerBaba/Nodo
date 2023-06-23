package owner.yacer.nodoproject.domain.di

import dagger.Module
import dagger.Provides
import owner.yacer.nodoproject.data.repository.LocalRepositoryImpl
import owner.yacer.nodoproject.domain.interfaces.LocalRepository

@Module
class UseCasesModule {
    @Provides
    fun provideLocalRepository(): LocalRepository = LocalRepositoryImpl
}