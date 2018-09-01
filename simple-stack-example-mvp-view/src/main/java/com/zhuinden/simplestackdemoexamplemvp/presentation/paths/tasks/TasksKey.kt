package com.zhuinden.simplestackdemoexamplemvp.presentation.paths.tasks

import android.view.View
import com.zhuinden.simplestack.ScopedServices
import com.zhuinden.simplestack.navigator.ViewChangeHandler
import com.zhuinden.simplestack.navigator.changehandlers.SegueViewChangeHandler
import com.zhuinden.simplestackdemoexamplemvp.R
import com.zhuinden.simplestackdemoexamplemvp.application.Injector
import com.zhuinden.simplestackdemoexamplemvp.application.Key
import com.zhuinden.simplestackdemoexamplemvp.util.scoping.ServiceProvider
import kotlinx.android.parcel.Parcelize

@Parcelize
data class TasksKey(val placeholder: String) : Key, ServiceProvider.HasServices {
    override fun getScopeTag(): String = "Tasks"

    override fun bindServices(serviceBinder: ScopedServices.ServiceBinder) {
        serviceBinder.add(TasksView.CONTROLLER_TAG, Injector.get().tasksPresenter())
    }

    constructor() : this("")

    override fun layout(): Int = R.layout.path_tasks

    override val isFabVisible: Boolean
        get() = true

    override fun viewChangeHandler(): ViewChangeHandler = SegueViewChangeHandler()

    override fun menu(): Int = R.menu.tasks_fragment_menu

    override fun navigationViewId(): Int = R.id.list_navigation_menu_item

    override fun shouldShowUp(): Boolean = false

    override fun fabClickListener(view: View): View.OnClickListener =
        View.OnClickListener{  v ->
            val tasksView = view as TasksView
            tasksView.openAddNewTask()
        }

    override fun fabDrawableIcon(): Int = R.drawable.ic_add
}
