<script>
import GlassObjectView from "@/components/GlassObjectView";
import MathUtil from "@/game/MathUtil";
import ArrayUtil from "@/game/ArrayUtil";

export default {
  name: 'GameView',
  components: {
    GlassObjectView
  },

  data() {
    return {
      availableColors: [
        'purple',
        'blue',
        'cyan',
        'green',
        'greenyellow',
        'yellow',
        'orange',
        'red',
      ],
      glassCount: 4,
      glassHeight: 100,
      glassWidth: 70,
      glassMaxLayers: 4,
      glassList: [],

      selectedGlassIndex: null,
      stepCount: 0,
      victory: false
    }
  },

  mounted() {
    this.onStartClick();
  },

  methods: {

    onGlassClick(index) {
      if (index === this.selectedGlassIndex) {
        this.selectedGlassIndex = null;
        return;
      }

      if (this.selectedGlassIndex == null) {

        const sourceGlass = this.glassList[index];

        if (sourceGlass.layers.length > 0) {
          this.selectedGlassIndex = index;
        }

      } else {
        const targetGlass = this.glassList[index];

        if (targetGlass.layers.length >= this.glassMaxLayers) {
          return;
        }

        const sourceGlass = this.glassList[this.selectedGlassIndex];

        targetGlass.layers.push(
            sourceGlass.layers.splice(sourceGlass.layers.length - 1, 1)[0]
        );

        this.stepCount += 1;
        this.selectedGlassIndex = null;

        this.checkWinCondition();
      }
    },

    onStartClick() {
      this.selectedGlassIndex = null;
      this.stepCount = 0;
      this.victory = false;
      let glassList = this.generateGlassList();

      while (this.isPuzzleComplete(glassList)) {
        glassList = this.generateGlassList()
      }
      this.glassList = glassList;
    },

    /**
     * @typedef LiquidLayer
     * @property {String} color
     *
     * @typedef Glass
     * @property {Array<LiquidLayer>} layers
     *
     * @return {Array<Glass>}
     */
    generateGlassList() {
      const levelColors = [];

      while (levelColors.length < this.glassCount) {
        const i = MathUtil.randomIntRange(0, this.availableColors.length - 1);
        const color = this.availableColors[i];
        if (levelColors.indexOf(color) === -1) {
          levelColors.push(color);
        }
      }

      const colors = [];

      // Fill array with all layers, which later will be distributed between glasses
      for (let i = 0; i < levelColors.length; i++) {
        for (let j = 0; j < this.glassMaxLayers - 1; j++) {
          colors.push(levelColors[i]);
        }
      }

      // Mix array with colors

      ArrayUtil.shuffle(colors);

      return ArrayUtil.splitArray(colors, this.glassMaxLayers - 1)
          .map(layerColors => {
            return {
              layers: layerColors.map(c => {
                return {
                  color: c
                }
              })
            }
          });
    },

    checkWinCondition() {
      if (this.isPuzzleComplete(this.glassList)) {
        this.victory = true;
      }
    },

    isPuzzleComplete(glassList) {
      let complete = true;

      for (let i = 0; i < glassList.length; i++) {
        const glass = glassList[i];

        if (glass.layers.length === 0
            || glass.layers.length >= this.glassMaxLayers) {
          complete = false;
          break;
        }


        const color = glass.layers[0].color;

        for (let j = 0; j < glass.layers.length; j++) {
          if (glass.layers[j].color !== color) {
            complete = false;
            break;
          }
        }
      }
      return complete;
    }
  }
}
</script>

<template>
  <div>
    <b>Color - Puzzle</b>
    <button @click="onStartClick">Restart</button>
  </div>

  <div>
    Steps: {{ stepCount }}
  </div>

  <div class="game-container">
    <div v-for="(g, index) in glassList" :key="index" style="display: inline-block; margin: 30px;">
      <GlassObjectView :topWidth="glassWidth" :height="glassHeight" :max-layers="glassMaxLayers" :layers="g.layers"
                       :class="{'wiggle': selectedGlassIndex === index || victory}"
                       @click="victory ? null : onGlassClick(index)"/>
    </div>
  </div>
</template>

<style scoped>

@keyframes wiggle {
  0% {
    transform: rotate(0deg);
  }
  25% {
    transform: rotate(5deg);
  }
  50% {
    transform: rotate(-5deg);
  }
  75% {
    transform: rotate(5deg);
  }
  100% {
    transform: rotate(0deg);
  }
}

.wiggle {
  display: inline-block;
  animation: wiggle 0.5s infinite;
}

.game-container {
  padding: 10px;
  background-color: black;
}
</style>