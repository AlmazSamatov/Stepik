package almaz.stepik.DataClasses

import com.google.gson.annotations.SerializedName;

data class Course(@SerializedName("course_title") val courseTitle: String,
                  @SerializedName("course_cover") val courseCover: String,
                  val course: Int)

data class Meta(val page: Int,
                @SerializedName("has_next") val hasNext: Boolean,
                @SerializedName("has_previous") val hasPrevious: Boolean)

data class Response(val meta: Meta,
                    @SerializedName("search-results") val searchResults: List<Course>)