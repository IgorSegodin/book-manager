<script>
import GameUtil from "@/game/GameUtil";

export default {
  name: 'GlassObjectView',

  props: {
    topWidth: {
      type: Number,
      required: true
    },
    height: {
      type: Number,
      required: true
    },
    maxLayers: {
      type: Number,
      required: true
    },
    glassAngle: {
      type: Number,
      default: 82
    },
    layers: {
      type: Array,
      default: () => [],
      validator(value) {
        // TODO check if both validator and type can work together
        return true;
        // return value === null
        //     || value === undefined
      }
    }
  },

  computed: {

    bottomWidth() {
      return GameUtil.calcGlassBottomWidth({
        height: this.height,
        topWidth: this.topWidth,
        angle: this.glassAngle
      });
    },

    glassVolume() {
      return (this.topWidth + this.bottomWidth) / 2 * this.height;
    },

    glassSvgPath() {
      const bottomMargin = (this.topWidth - this.bottomWidth) / 2;

      return this.pointsToSvgPath([
        {x: 0, y: 0},
        {x: this.topWidth, y: 0},
        {x: this.topWidth - bottomMargin, y: this.height},
        {x: bottomMargin, y: this.height},
      ]);
    },

    layerVolume() {
      /*
       5% should be always empty, just for aesthetic (we never fill the glass till the top)
      */
      return this.glassVolume * 0.95 / this.maxLayers;
    },

    /**
     * @typedef {Object} LayerSvgData
     * @property {String} color
     * @property {String} path
     *
     * @return {Array<LayerSvgData>}
     */
    listLayerSvgData() {
      const list = [];

      let previousLayerTopWidth = this.bottomWidth;
      let previousHeight = 0;

      for (let i = 0; i < this.layers.length; i++) {
        const layerHeight = GameUtil.calcLayerHeight({
          liquidArea: this.layerVolume,
          bottomWidth: previousLayerTopWidth,
          angle: this.glassAngle
        });
        const layerBottomWidth = previousLayerTopWidth;
        const layerTopWidth = GameUtil.calcGlassTopWidth({
          height: layerHeight,
          bottomWidth: layerBottomWidth,
          angle: this.glassAngle
        });

        const layerBottomMargin = (this.topWidth - layerBottomWidth) / 2;
        const layerTopMargin = (this.topWidth - layerTopWidth) / 2;

        const yBottom = this.height - previousHeight;
        const yTop = yBottom - layerHeight;
        const x1Bottom = layerBottomMargin;
        const x2Bottom = layerBottomMargin + layerBottomWidth;
        const x1Top = layerTopMargin;
        const x2Top = layerTopMargin + layerTopWidth;

        list.push(
            {
              color: this.layers[i].color,
              path: this.pointsToSvgPath([
                {x: x1Top, y: yTop},
                {x: x2Top, y: yTop},
                {x: x2Bottom, y: yBottom},
                {x: x1Bottom, y: yBottom},
              ])
            }
        );
        previousLayerTopWidth = layerTopWidth;
        previousHeight += layerHeight;
      }

      return list;
    }
  },

  methods: {

    /**
     * @typedef Point
     * @property {Number} x
     * @property {Number} y
     * @param {Array<Point>} points
     * @return {String}
     */
    pointsToSvgPath(points) {
      return points.map((p, i) => {
        return `${i === 0 ? 'M' : 'L'} ${p.x} ${p.y}`;
      })
          .join(' ') + ' z';
    }

  }
}
</script>

<template>
  <div style="padding: 2px; display: inline-block">
    <svg :width="topWidth" :height="height" overflow="visible" style="overflow: visible;">
      <g>

        <path v-for="(p, index) in listLayerSvgData" :key="index" :fill="p.color" fill-opacity="0.9" :d="p.path"/>

        <!-- Glass -->
        <path fill-opacity="0" stroke="#A2A2A2" stroke-width="1.5" :d="glassSvgPath"/>
      </g>
    </svg>
  </div>
</template>
