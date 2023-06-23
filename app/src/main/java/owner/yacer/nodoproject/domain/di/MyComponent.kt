package owner.yacer.nodoproject.domain.di

import dagger.Component
import owner.yacer.nodoproject.ui.viewmodels.CreateNewNoteViewModel
import owner.yacer.nodoproject.ui.viewmodels.NoteFragmentViewModel
import owner.yacer.nodoproject.ui.viewmodels.PreviewViewModel
import owner.yacer.nodoproject.ui.viewmodels.TodoFragmentViewModel

@Component(modules = [UseCasesModule::class])
interface MyComponent {
    fun getCreateNoteViewModel():CreateNewNoteViewModel
    fun getNoteViewModel():NoteFragmentViewModel
    fun getPreviewViewModel():PreviewViewModel
    fun getTodoViewModel():TodoFragmentViewModel
}