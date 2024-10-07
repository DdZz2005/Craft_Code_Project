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
        // Установи данные в виджеты ViewHolder'а
        holder.itemView.setOnClickListener {
            onItemClick(request) // Передаём выбранную заявку в колбэк
        }
    }

    override fun getItemCount(): Int {
        return requestList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // Здесь привяжи виджеты для отображения данных заявки
        val requestDescription = itemView.findViewById<TextView>(R.id.requestDescription)
        val requestDate = itemView.findViewById<TextView>(R.id.requestDate)
    }
}

