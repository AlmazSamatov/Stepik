package almaz.stepik.DataClasses

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.graphics.drawable.Drawable
import com.google.gson.annotations.SerializedName;

@Entity(tableName = "course")
data class Course(@SerializedName("course_title") val courseTitle: String?,
                  @SerializedName("course_cover") val courseCover: String?,
                  @PrimaryKey val id: Int,
                  var isFavorite: Boolean = false)

data class Meta(val page: Int,
                @SerializedName("has_next") val hasNext: Boolean,
                @SerializedName("has_previous") val hasPrevious: Boolean)

data class Response(val meta: Meta,
                    @SerializedName("search-results") val searchResults: MutableList<Course>)