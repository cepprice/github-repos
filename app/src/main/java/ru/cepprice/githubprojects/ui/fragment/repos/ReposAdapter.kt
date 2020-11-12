package ru.cepprice.githubprojects.ui.fragment.repos

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import ru.cepprice.githubprojects.data.local.model.RepoView
import ru.cepprice.githubprojects.databinding.ListItemRepoBinding

class ReposAdapter(
    private val listener: (RepoView) -> Unit
) : RecyclerView.Adapter<ReposAdapter.RepoViewHolder>() {

    private val repos = ArrayList<RepoView>()

    class RepoViewHolder(val binding: ListItemRepoBinding) : RecyclerView.ViewHolder(binding.root)

    fun setRepos(repos: ArrayList<RepoView>) {
        this.repos.clear()
        this.repos.addAll(repos)
        notifyDataSetChanged()
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
        holder.itemView.setOnClickListener { listener(currentRepo) }
        holder.binding.executePendingBindings()
    }

    override fun getItemCount(): Int = repos.size
}