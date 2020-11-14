package ru.cepprice.githubprojects.ui.fragment.repos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.cepprice.githubprojects.data.local.model.RepoView
import ru.cepprice.githubprojects.databinding.ListItemRepoBinding

class ReposAdapter : RecyclerView.Adapter<ReposAdapter.RepoViewHolder>() {

    private val repos = ArrayList<RepoView>()
    private lateinit var listener: (String) -> Boolean

    class RepoViewHolder(val binding: ListItemRepoBinding) : RecyclerView.ViewHolder(binding.root)

    fun setRepos(repos: ArrayList<RepoView>) {
        if (! this.repos.containsAll(repos)) this.repos.clear()
        this.repos.addAll(repos)
        notifyDataSetChanged()
    }

    fun deleteRepo(name: String) {
        repos.removeAll(repos.filter { it.name == name })
        notifyDataSetChanged()
    }

    fun addListener(listener: (String) -> Boolean) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RepoViewHolder {
        val binding = ListItemRepoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RepoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RepoViewHolder, position: Int) {
        val currentRepo = repos[position]
        holder.binding.repo = currentRepo
        holder.itemView.setOnLongClickListener { listener(currentRepo.name) }
        holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int = repos.size
}