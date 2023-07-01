package owner.yacer.nodoproject.domain.di

import dagger.Component
import owner.yacer.nodoproject.ui.viewmodels.*

@Component(modules = [UseCasesModule::class])
interface MyComponent {
    fun getCreateNoteViewModel():CreateNewNoteViewModel
    fun getNoteViewModel():NoteFragmentViewModel
    fun getPreviewViewModel():PreviewViewModel
    fun getTodoViewModel():TodoFragmentViewModel
    fun getMainViewModel():MainViewModel
}