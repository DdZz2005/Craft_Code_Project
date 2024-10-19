package API_service

import InventoryRequest
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.craft_code_mobile_project.R

class InventoryRequestAdapter(
    private val requestList: List<InventoryRequest>,
    private val onItemClick: (InventoryRequest, Boolean) -> Unit // Обновляем сигнатуру функции
) : RecyclerView.Adapter<InventoryRequestAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_request, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val request = requestList[position]

        // Устанавливаем описание и дату
        holder.requestDescription.text = "Заявка от ${request.employee}"
        holder.requestDate.text = "Срок: ${request.deadline}"

        // Проверяем статус заявки и меняем иконку
        if (request.status == "completed") {
            holder.requestIcon.setImageResource(R.drawable.img_3)  // Иконка завершенной заявки
            holder.itemView.setOnClickListener {
                // Загрузка PDF для завершенных заявок
                onItemClick(request, true)  // Передаем параметр напрямую
            }
        } else {
            holder.requestIcon.setImageResource(R.drawable.img_6)  // Иконка незавершенной заявки
            holder.itemView.setOnClickListener {
                // Переход к деталям для незавершенных заявок
                onItemClick(request, false)  // Передаем параметр напрямую
            }
        }
    }

    override fun getItemCount(): Int {
        return requestList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val requestDescription: TextView = itemView.findViewById(R.id.requestDescription)
        val requestDate: TextView = itemView.findViewById(R.id.requestDate)
        val requestIcon: ImageView = itemView.findViewById(R.id.requestIcon)  // Поле для иконки
    }
}
