import XImage from "./assets/images/X.jpg"
import HImage from "./assets/images/H.jpg"
import BImage from "./assets/images/B.jpg"
import PImage from "./assets/images/P.jpg"
import TImage from "./assets/images/T.jpg"
import OImage from "./assets/images/O.jpg"
import RImage from "./assets/images/R.jpg"
import EImage from "./assets/images/E.jpg"
import YImage from "./assets/images/Y.jpg"
import QImage from "./assets/images/Q.jpg"
import WImage from "./assets/images/W.jpg"
import UImage from "./assets/images/U.jpg"

function RenderGame(props) {

    const images = {
        X: XImage, H: HImage, B: BImage, P: PImage,
        T: TImage, O: OImage, R: RImage, E: EImage,
        Y: YImage, Q: QImage, W: WImage, U: UImage
    };

    return (
        <div>
            <table >
                <tbody>
                    {props.game.map.map((row, rowIndex) => (
                        <tr key={rowIndex}>
                            {row.map((col, colIndex) => (
                                <td key={`${rowIndex}-${colIndex}`}>
                                    <img src={images[col]} alt={col} width="50" height="50"></img>
                                </td>
                            ))}
                        </tr>
                    ))}
                </tbody>
            </table>
        </div>
    );
}

export default RenderGame;