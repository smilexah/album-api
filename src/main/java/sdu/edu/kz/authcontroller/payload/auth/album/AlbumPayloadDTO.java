package sdu.edu.kz.authcontroller.payload.auth.album;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AlbumPayloadDTO {
    @NotBlank
    @Schema(description = "Album name", example = "Уроборос: Улица 36", requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;
    @NotBlank
    @Schema(description = "Album description", example = "первая половина третьего альбома казахстанского рэпера Скриптонита из двух частей, обе из которых вышли 16 декабря 2017 года, аккурат в день независимости Казахстана. После выхода пластинки Жалелов заявил, что в связи с тем, что для него рэп как жанр себя изжил и ему пока что нечего сказать в этом формате, больших сольных работ от него не стоит ожидать в ближайшие два-три года.", requiredMode = Schema.RequiredMode.REQUIRED)
    private String description;
}
