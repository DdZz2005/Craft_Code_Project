package API_service

import InventoryRequest
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.craft_code_mobile_project.R

class InventoryRequestAdapter(
    private val requestList: List<InventoryRequest>,
    private val onItemClick: (InventoryRequest) -> Unit
) : RecyclerView.Adapter<InventoryRequestAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_request, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val request = requestList[position]
        holder.requestDescription.text = "Заявка от ${request.employee}"
        holder.requestDate.text = "Срок: ${request.deadline}"
        holder.itemView.setOnClickListener {
            onItemClick(request)
        }
    }

    override fun getItemCount(): Int {
        return requestList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val requestDescription: TextView = itemView.findViewById(R.id.requestDescription)
        val requestDate: TextView = itemView.findViewById(R.id.requestDate)
    }
}
