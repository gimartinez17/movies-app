package com.gmart.data.model

import com.gmart.domain.model.Genre

class GenreEntity(
    var id: Int,
    var name: String,
)

fun GenreEntity.mapToModel() = Genre(
    id = id,
    name = name
)

fun GenreEntity.mapToModelWithImage(isMovie: Boolean) = Genre(
    id = id,
    name = name,
    image = if (isMovie) getMovieGenreImg(id) else getTvGenreImg(id)
)

private fun getMovieGenreImg(id: Int): String {
    return when (id) {
        28 -> "/dqK9Hag1054tghRQSqLSfrkvQnA.jpg"
        12 -> "/9DeGfFIqjph5CBFVQrD6wv9S7rR.jpg"
        16 -> "/wXsQvli6tWqja51pYxXNG1LFIGV.jpg"
        35 -> "/askg3SMvhqEl4OL52YuvdtY40Yb.jpg"
        80 -> "/kXfqcdQKsToO0OUXHcrrNCHDBzO.jpg"
        99 -> "/vrx9KIwXm7nxz49Fs9iTjPgxq7A.jpg"
        18 -> "/tmU7GeKVybMWFButWEGl2M4GeiP.jpg"
        10751 -> "/b1Y8SUb12gPHCSSSNlbX4nB3IKy.jpg"
        14 -> "/n5A7brJCjejceZmHyujwUTVgQNC.jpg"
        36 -> "/3f92DMBTFqr3wgXpfxzrb0qv8nG.jpg"
        27 -> "/uif5fUshJrXyyDzfpzp1DLw3N0S.jpg"
        10402 -> "/ySaK6hpbCc2OE13ac7ovhgWwR5u.jpg"
        9648 -> "/dYjZ27hDw2QFaEIfzbNGwW0IkV9.jpg"
        10749 -> "/zvOJawrnmgK0sL293mOXOdLvTXQ.jpg"
        878 -> "/xJHokMbljvjADYdit5fK5VQsXEG.jpg"
        10770 -> "/p0boFm5Z1HzO6LHMBuLYUM48iVV.jpg"
        53 -> "/hiKmpZMGZsrkA3cdce8a7Dpos1j.jpg"
        10752 -> "/dVr11o9or7AS8AMPfwjSpEU83iU.jpg"
        37 -> "/5Lbm0gpFDRAPIV1Cth6ln9iL1ou.jpg"
        else -> ""
    }
}

private fun getTvGenreImg(id: Int): String {
    return when (id) {
        10759 -> "/2OMB0ynKlyIenMJWI2Dy9IWT4c.jpg"
        16 -> "/rBF8wVQN8hTWHspVZBlI3h7HZJ.jpg"
        35 -> "/slVhck5n2GwlgPFWKJGfqiYyzCd.jpg"
        80 -> "/wiE9doxiLwq3WCGamDIOb2PqBqc.jpg"
        99 -> "/2Nwbv0hrN8sThLvgooShcPqmFrO.jpg"
        18 -> "/900tHlUYUkp7Ol04XFSoAaEIXcT.jpg"
        10751 -> "/70YdbMELM4b8x8VXjlubymb2bQ0.jpg"
        10762 -> "/zjBzGqpR3w8fPox84pQHQ20E34d.jpg"
        9648 -> "/6YANELLOCepacNcfJ9lj43NvvrZ.jpg"
        10763 -> "/jO803koX4pYjGuxjOkLytCusuJm.jpg"
        10764 -> "/hEUigxkQ6JTsKDUab8TL8trGOAc.jpg"

        10765 -> "/thLAoL6VeZGmCyDpCOeoxLvA8yS.jpg"
        10766 -> "/mXVr88l7877i0P4cLQl1mUvaXGJ.jpg"
        10767 -> "/jO803koX4pYjGuxjOkLytCusuJm.jpg"
        10768 -> "/uCqXSfHymdbDMsFx8t0u0OPSuve.jpg"
        37 -> "/xHkOKPUe3ioXyPIe5odyL6o6cp4.jpg"
        else -> ""
    }
}